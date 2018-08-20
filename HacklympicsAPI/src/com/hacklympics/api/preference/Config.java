package com.hacklympics.api.preference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    
    public static final String CONFIG_FILENAME = "config.properties";
    
    public static final String DEFAULT_SERVER_HOSTNAME = "127.0.0.1";
    public static final int DEFAULT_SERVER_PORT = 8000;
    public static final int DEFAULT_EVENT_LISTENER_PORT = 8001;
    
    public static final double DEFAULT_GENGRP_SNAPSHOT_QUALITY = 0.25;
    public static final double DEFAULT_SPEGRP_SNAPSHOT_QUALITY = 0.50;
    public static final int DEFAULT_GENGRP_SYNC_FREQUENCY = 5;
    public static final int DEFAULT_SPEGRP_SYNC_FREQUENCY = 3;
    
    private final List<Double> snapshotQualityOptions;
    private final List<Integer> syncFrequencyOptions;
    
    private static Config config;
    private File propertiesFile;
    private Properties properties;
    
    // Network-related properties.
    public final String serverHostname;
    public final int serverPort;
    public final int eventListenerPort;
    
    // Snapshot image quality.
    private double genGrpSnapshotQuality;
    private double speGrpSnapshotQuality;
    
    // Snapshot & keystroke patches syncing frequency.
    private int genGrpSyncFrequency;
    private int speGrpSyncFrequency;
    
    private Config() {
        propertiesFile = new File(CONFIG_FILENAME);
        
        // If the properties file does not exist, create one.
        if (!propertiesFile.exists()) {
            properties = new Properties();
            properties.setProperty("Server.hostname", DEFAULT_SERVER_HOSTNAME);
            properties.setProperty("Server.port", Integer.toString(DEFAULT_SERVER_PORT));
            properties.setProperty("EventListener.port", Integer.toString(DEFAULT_EVENT_LISTENER_PORT));
            
            properties.setProperty("GeneralGroup.snapshotQuality", Double.toString(DEFAULT_GENGRP_SNAPSHOT_QUALITY));
            properties.setProperty("SpecialGroup.snapshotQuality", Double.toString(DEFAULT_SPEGRP_SNAPSHOT_QUALITY));
            properties.setProperty("GeneralGroup.syncFrequency", Integer.toString(DEFAULT_GENGRP_SYNC_FREQUENCY));
            properties.setProperty("SpecialGroup.syncFrequency", Integer.toString(DEFAULT_SPEGRP_SYNC_FREQUENCY));
            
            save(propertiesFile);
        }
        
        // Load user configuration from the properties file.
        properties = load(propertiesFile);
        
        // Load critical network configuration.
        serverHostname = properties.getProperty("Server.hostname");
        serverPort = Integer.parseInt(properties.getProperty("Server.port"));
        eventListenerPort = Integer.parseInt(properties.getProperty("EventListener.port"));
        
        // Load proctor configuration.
        genGrpSnapshotQuality = Double.parseDouble(properties.getProperty("GeneralGroup.snapshotQuality"));
        speGrpSnapshotQuality = Double.parseDouble(properties.getProperty("SpecialGroup.snapshotQuality"));
        genGrpSyncFrequency = Integer.parseInt(properties.getProperty("GeneralGroup.syncFrequency"));
        speGrpSyncFrequency = Integer.parseInt(properties.getProperty("SpecialGroup.syncFrequency"));
        
        
        // Populate available options of snapshot quality and sync frequency.
        snapshotQualityOptions = new ArrayList<>();
        syncFrequencyOptions = new ArrayList<>();
        
        snapshotQualityOptions.add(0.15);
        snapshotQualityOptions.add(0.25);
        snapshotQualityOptions.add(0.35);
        snapshotQualityOptions.add(0.50);
        snapshotQualityOptions.add(0.75);
        
        syncFrequencyOptions.add(3);
        syncFrequencyOptions.add(5);
        syncFrequencyOptions.add(8);
        syncFrequencyOptions.add(10);
        syncFrequencyOptions.add(13);
    }
    
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        
        return config;
    }
    
    
    /**
     * Saves current state of properties to the specified file.
     * Note that the network configuration shoule be unmodifiable during runtime,
     * So only the proctor configuration will be updated and written to files. 
     * @param f file to write to.
     */
    public void save(File f) {
        try {
            properties.store(new FileWriter(f), "Hacklympics client properties");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Loads properties from the specified file.
     * @param f file to load from.
     * @return properties loaded from the file.
     */
    public Properties load(File f) {
        Properties p = new Properties();
        
        try {
            p.load(new FileReader(f));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        return p;
    }
    
    
    /**
     * Gets the FQDN of the server in the format of "http://%s:%s".
     * @return FQDN of the server.
     */
    public String getURL() {
        return String.format("http://%s:%s", serverHostname, serverPort);
    }
    
    
    public List<Double> getSnapshotQualityOptions() {
        return snapshotQualityOptions;
    }
    
    public List<Integer> getSyncFrequencyOptions() {
        return syncFrequencyOptions;
    }
    
    
    /**
     * Gets snapshot quality of the generic group.
     * @return snapshot quality of the generic group.
     */
    public double getGenGrpSnapshotQuality() {
        return genGrpSnapshotQuality;
    }
    
    /**
     * Gets snapshot quality of the special group.
     * @return snapshot quality of the special group.
     */
    public double getSpeGrpSnapshotQuality() {
        return speGrpSnapshotQuality;
    }
    
    
    /**
     * Gets syncing frequency of the generic group
     * (shared by both snapshots and keystroke patches).
     * @return syncing frequency of the generic group.
     */
    public int getGenGrpSyncFrequency() {
        return genGrpSyncFrequency;
    }
    
    /**
     * Gets syncing frequency of the special group.
     * (shared by both snapshots and keystroke patches).
     * @return syncing frequency of the special group.
     */
    public int getSpeGrpSyncFrequency() {
        return speGrpSyncFrequency;
    }
    
    /**
     * Sets snapshot quality of the generic group.
     * @param genGrpSnapshotQuality new snapshot quality of the generic group.
     */
    public void setGenGrpSnapshotQuality(double genGrpSnapshotQuality) {
        this.genGrpSnapshotQuality = genGrpSnapshotQuality;
    }
    
    /**
     * Gets snapshot quality of the special group.
     * @param speGrpSnapshotQuality new snapshot quality of the special group.
     */
    public void getSpeGrpSnapshotQuality(double speGrpSnapshotQuality) {
        this.speGrpSnapshotQuality = speGrpSnapshotQuality;
    }
    
    
    /**
     * Gets syncing frequency of the generic group
     * (shared by both snapshots and keystroke patches).
     * @param genGrpSyncFrequency new syncing frequency of the generic group.
     */
    public void getGenGrpSyncFrequency(int genGrpSyncFrequency) {
        this.genGrpSyncFrequency = genGrpSyncFrequency;
    }
    
    /**
     * Gets syncing frequency of the special group.
     * (shared by both snapshots and keystroke patches).
     * @param speGrpSyncFrequency new syncing frequency of the special group.
     */
    public void getSpeGrpSyncFrequency(int speGrpSyncFrequency) {
        this.speGrpSyncFrequency = speGrpSyncFrequency;
    }
    
}