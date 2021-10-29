package com.example.gav_fx.components.bottompane;

import com.example.gav_fx.core.*;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;

public class CPUUtilizationUpdater implements Runnable {
    
    // index[core number] -> utilization [0, 100]
    private final IntegerProperty coreUtilizationValues[];
    private final String powershellCommand = "(Get-WmiObject -Query \"select Name, PercentProcessorTime from Win32_PerfFormattedData_PerfOS_Processor\") | " +
                                             "foreach-object { write-host \"$($_.Name): $($_.PercentProcessorTime)\" };";
    private final int cores = Runtime.getRuntime().availableProcessors();
    private final long UPDATE_INTERVAL_MS = 1000;
    
    // A thread running this class really shouldn't get instantiated more than once...
    public CPUUtilizationUpdater() {
        
        coreUtilizationValues = new SimpleIntegerProperty[cores + 1]; // +1 because one of values is Total(see bottom of this class)
        for (int i = 0; i < coreUtilizationValues.length; i++) {
            coreUtilizationValues[i] = new SimpleIntegerProperty();
            coreUtilizationValues[i].set(-1);
        }
    }
    
    @Override
    public void run() {
        LOG.out(" -> ", this.getClass().getSimpleName() + " started.", OutputType.DEBUG);
        while (!WorkerController.STOP_THREAD.get()) {
            try {
                long t0 = System.currentTimeMillis();
    
                update();
    
                long timeTakenToUpdate = (System.currentTimeMillis() - t0) / 1000;
                long sleepTime = UPDATE_INTERVAL_MS - timeTakenToUpdate;
                Tools.sleep(sleepTime);
            }
            catch (Exception e) {
                LOG.error("Something went wrong when updating CPU utilization, " +
                        "ignoring and continuing.\n Short error message: " + e.getLocalizedMessage() +
                        " on line" + e.getStackTrace()[0]);
            }
        }
        LOG.out(" -> ", this.getClass().getSimpleName() + " finished.", OutputType.DEBUG);
    }
    
    public void update() {
        PowerShellResponse response = PowerShell.executeSingleCommand(powershellCommand);
        String rawOutput = response.getCommandOutput();
        
        Arrays.stream(rawOutput.split("\n"))
                .map(this::mapToPairOfValues)
                .forEach(this::updateValues);
    }
    
    private void updateValues(Tools.Tuple1<Integer> valuePair) {
        int coreId = valuePair.getLeft();
        int coreUtil = valuePair.getRight();
        Platform.runLater(() -> coreUtilizationValues[coreId].setValue(coreUtil));
    }
    
    private Tools.Tuple1<Integer> mapToPairOfValues(String line) {
        String[] pair = line.split(":");
        int coreId = pair[0].trim().equalsIgnoreCase("_total") ?
                cores : // put at the end of array, since values.length-1 == cores
                Integer.parseInt(pair[0].trim());
        int coreUtil = Integer.parseInt(pair[1].trim());
        return new Tools.Tuple1<>(coreId, coreUtil);
    }
    
    public IntegerProperty[] getProperties() { return coreUtilizationValues; }
    
    //public static class CPUUtilObservables extends ObjectBinding<IntegerProperty> {
    //    private final Worker.ExecutorThreadInfo value;
    //
    //    public ThreadCPUTimeObservable(String initialValue) {
    //        this.value = new Worker.ExecutorThreadInfo(initialValue);
    //        bind(this.value.property());
    //    }
    //
    //    @Override
    //    protected Worker.ExecutorThreadInfo computeValue() {
    //        return value;
    //    }
    //}
    
    /**
     * Sample command output, this is the string to parse:
     * 0: 0
     * 1: 4
     * 10: 0
     * 11: 10
     * 12: 0
     * 13: 0
     * 14: 0
     * 15: 4
     * 2: 4
     * 3: 4
     * 4: 4
     * 5: 0
     * 6: 0
     * 7: 4
     * 8: 0
     * 9: 4
     * _Total: 1
     *
     * The last line (_Total) probably outputs the average/combined value, or something.
     */
}
