package com.example.gav_fx.core;

import com.example.gav_fx.graph.Edge;
import com.example.gav_fx.graph.MyGraph;
import com.example.gav_fx.graph.Node;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphVertexChangeEvent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerController implements Runnable, StateObservable, GraphChangeObserver {
    
    public static int PROCESSORS = 3;
    public static int BATCH_SIZE = 3; // // TODO hardcoded for now for testing purposes on smaller graphs (should depend on graph size or let used define it)
    
    // WORK_BATCHES: work that must be done by threads
    // PROCESSED_BATCHES: work that has been processed (popped from work and inserted into processedWork)
    // batchBuffer: buffer when adding new nodes
    public static Deque<WorkBatch> WORK_BATCHES = new ConcurrentLinkedDeque<>();
    public static Deque<WorkBatch> PROCESSED_BATCHES = new ConcurrentLinkedDeque<>();
    private WorkBatch batchBuffer = new WorkBatch();
    
    OutputType outputType = OutputType.ALGO_CONTROLLER;
    
    // TODO: should these really be final if we can change number of processors anytime we want.. probably not?
    // TODO: update on change of nodes
    static final CyclicBarrier BARRIER = new CyclicBarrier(PROCESSORS + 1);
    //final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(PROCESSORS);
    private boolean WORKERS_STARTED = false;
    final List<Worker> WORKERS = new ArrayList<>(PROCESSORS);
    public static final AtomicBoolean NEXT_ROUND_BUTTON_PRESSED = new AtomicBoolean(false);
    
    public static int currentStateIndex = 0; // atomic?
    public static int totalStates = 1;
    
    MyGraph graph;
    Algorithm algo;
    
    public static AtomicBoolean STOP_THREAD = new AtomicBoolean(false);
    public static volatile AtomicBoolean PAUSE = new AtomicBoolean(true);
    public static final Object PAUSE_LOCK = new Object();
    public static int TIMEOUT_BETWEEN_ROUNDS = 100;
    
    private RoundStatisticsData currentRoundStats;
    
    public WorkerController(MyGraph graph /*, Algorithm algo */) {
        this.graph = graph;
        //this.algo = algo;
        
        MyGraph.getInstance().addObserver(this);
        
        initProcessors();
        WORK_BATCHES.add(batchBuffer);
//        initializeWorkBatches();
    }
    
    public static void doOneRoundOfWork() {
        System.out.println("one round of work start");
        WorkerController.currentStateIndex = WorkerController.totalStates - 1;
        synchronized (WorkerController.PAUSE_LOCK) {
            NEXT_ROUND_BUTTON_PRESSED.compareAndSet(false, true);
            //WorkerController.PAUSE.compareAndSet(true, false);
            WorkerController.PAUSE_LOCK.notify();
        }
    }
    
    public void setAlgorithm(Algorithm algo) {
        this.algo = algo;
        WORKERS.forEach(w -> w.setAlgorithm(algo));
    }
    
    public void signalShutDown() {
        WorkerController.STOP_THREAD.set(true);
        synchronized (WorkerController.PAUSE_LOCK) { WorkerController.PAUSE_LOCK.notify(); }
        synchronized (WorkerController.BARRIER) { BARRIER.notifyAll(); }
        //THREAD_POOL.shutdown();
    }
    
    @Override
    public void run() {
        Thread.currentThread().setName("CNTRLR");
        LOG.out("\n + ", "Controller thread started", outputType);
        
        LOG.out("\n -> ", "Starting all workers...", outputType);
        for (Worker worker : WORKERS) worker.start();
        
        while (true)
        {
            // Wait at start
            if (PAUSE.getAcquire()) {
                LOG.warning("PAUSING");
                synchronized (PAUSE_LOCK) {
                    while (PAUSE.getAcquire() && !NEXT_ROUND_BUTTON_PRESSED.get()) {
                        if (STOP_THREAD.get()) break;
                        try { PAUSE_LOCK.wait(); }
                        catch (Exception e) { e.printStackTrace(); }
                    }
                    // woken up
                    // if woken up by button, then set the button press to false
                    // if not woken up by button, then don't do anything and just continue
                    boolean wasPressed = NEXT_ROUND_BUTTON_PRESSED.compareAndSet(true, false);
                }
                LOG.out("->", "CONTINUING", outputType);
            }
            
            // Wake up workers
            LOG.warning("Waking up workers");
            synchronized (Worker.WORKER_LOCK) { Worker.WORKER_LOCK.notifyAll(); }
            
            // Waiting for all Workers to finish processing
            try { WorkerController.BARRIER.await(); }
            catch (InterruptedException | BrokenBarrierException e) { e.printStackTrace(); }
            LOG.warning("ALL WORKERS DONE; BARRIER TIPPED");
            
            processRoundStatistics();
            
            if (STOP_THREAD.get()) break;
            
            // All workers done, do some processing
            incrementState();
            
            MyGraph.getInstance().getNodes().forEach(Node::updateStatesInfo);
            
            // switch references around to re-add work
            // work_batches should be empty at this point, and processed_batches should be full
            var tmpRef = WORK_BATCHES;
            WORK_BATCHES = PROCESSED_BATCHES;
            PROCESSED_BATCHES = tmpRef;
            LOG.warning("Switched batches; wrk_batch:" + WORK_BATCHES.size() + ", prcs_btchs: " + PROCESSED_BATCHES.size());
            
            LOG.warning("STATUS\n" +
                    " -> currentStateIndex="+currentStateIndex + "\n" +
                    " -> totalStates="+totalStates);
            
            // TODO
            //MenuPanel.nextBtn.setEnabled(AlgorithmController.PAUSE.get());
            //MenuPanel.prevBtn.setEnabled(AlgorithmController.PAUSE.get());
            
            Tools.sleep(TIMEOUT_BETWEEN_ROUNDS);
        }
        
        synchronized (Worker.WORKER_LOCK) { Worker.WORKER_LOCK.notifyAll(); }
        LOG.out("", "AlgorithmController thread terminated.", outputType);
    }
    
    // This method must be called BEFORE notifying observers about new state!
    private void processRoundStatistics() {
        long totalTimeElapsed = WORKERS.stream()
                .map(Worker::getTimeElapsedProcessingWorkBatches)
                .reduce(1L, Long::sum);
        currentRoundStats = new RoundStatisticsData(
                Math.max(1, currentStateIndex), // todo broken
                totalTimeElapsed,
                WORKERS.size());
    }
    
    public void setCurrentStateToIndex(int newCurrentStateIndex) {
        currentStateIndex = newCurrentStateIndex;
        observers.forEach(StateObserver::onStateChange);
    }
    
    private void incrementState() {
        System.out.println("INCREMENTING");
        totalStates++;
        currentStateIndex++;
        observers.forEach(obs -> obs.onNewState(currentRoundStats));
        observers.forEach(StateObserver::onStateChange);
    }
    
    private void initProcessors() {
        for (int i=0; i<PROCESSORS; i++) {
            WORKERS.add(new Worker(algo, "W-"+i));
        }
    }
    
    public void onNewAlgorithmSelected(Algorithm algo) {
        this.algo = algo; // is this field even necessary in this class?
        WORKERS.forEach(w -> w.setAlgorithm(algo));
    }
    
    public void initializeWorkBatches() {
        // Clear all batches first
        WORK_BATCHES.clear();
        PROCESSED_BATCHES.clear();
        
        // There is no work to be processed
        if (graph.getGraph().vertexSet().isEmpty()) {
            return;
        }
        
        // Divide work(Node) into batches(WorkBatches)
        int batchSize = BATCH_SIZE;
        Iterator<Node> nodeIter = this.graph.getNodes().stream().iterator();
        while(nodeIter.hasNext()) {
            final WorkBatch workBatch = new WorkBatch();
            final Set<Node> batch = new HashSet<>((int)(batchSize*1.1));
            int batchSizeLimit = batchSize;
            while(nodeIter.hasNext() && --batchSizeLimit >= 0) {
                Node n = nodeIter.next();
                n.setBatchParent(workBatch);
                batch.add(n);
            }
            workBatch.setWorkBatch(batch);
            WORK_BATCHES.add(workBatch);
        }
        
        // Just to be sure, check if all nodes have been assigned to a batch
        int nodes = graph.getGraph().vertexSet().size();
        int work = WORK_BATCHES.stream().map(WorkBatch::getNodesToProcess).mapToInt(Collection::size).sum();
        if (nodes != work) {
            String errMsg = "Error assigning work batches!\n" +
                    "Number of nodes: " + nodes + ", number of work: " + work;
            LOG.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }
    
    public void updatedProcessorsNumber(int processors) {
        //int deltaP = processors
    }
    
    Set<StateObserver> observers = new HashSet<>(8);
    
    @Override
    public void addObserver(StateObserver obsever) {
        this.observers.add(obsever);
    }
    
    @Override
    public void removeObserver(StateObserver observer) {
        this.observers.remove(observer);
    }
    
    @Override
    public void onGraphClear() {
        WorkerController.totalStates = 1;
        WorkerController.currentStateIndex = 0;
        
        initializeWorkBatches();
    }
    // TODO methods have the same body
    @Override
    public void onGraphImport() {
        WorkerController.totalStates = 1;
        WorkerController.currentStateIndex = 0;
        
        initializeWorkBatches();
    }
    
    @Override
    public void vertexAdded(GraphVertexChangeEvent<Node> e) {
        Node newNode = e.getVertex();
        this.addObserver(newNode);
        
        // since we are using Deque, finding a random
        // WorkBatch isn't scalable
        // So create a new one, and keep filling it until its full
        if (batchBuffer.getNodesToProcess().size() > BATCH_SIZE) {
            batchBuffer = new WorkBatch();
            WORK_BATCHES.add(batchBuffer);
        }
        // Bind them
        newNode.setBatchParent(batchBuffer);
        batchBuffer.addMoreWork(newNode);
    
        WorkerController.totalStates = WorkerController.currentStateIndex + 1;
    }
    
    @Override
    public void vertexRemoved(GraphVertexChangeEvent<Node> e) {
        WorkerController.totalStates = WorkerController.currentStateIndex + 1;
        Node nodeToRemove = e.getVertex();
        observers.remove(nodeToRemove); // CRUCIAL! reference to node needs to get removed!!!
        boolean removed = nodeToRemove.getBatchParent().getNodesToProcess().remove(nodeToRemove);
        if (!removed) {
            String errorMsg = "Node " + nodeToRemove + " not found and wasn't removed from any WorkBatch!";
            LOG.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }
    
    @Override public void onNewInformedNode() {}
    @Override public void onNewUninformedNode() {}
    
    @Override public void edgeAdded(GraphEdgeChangeEvent<Node, Edge> e) {}
    @Override public void edgeRemoved(GraphEdgeChangeEvent<Node, Edge> e) {}
}
