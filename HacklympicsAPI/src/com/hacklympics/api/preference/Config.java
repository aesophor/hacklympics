package com.hacklympics.api.preference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
    
    public static final String CONFIG_FILENAME = "config.properties";
    
    public static final String DEFAULT_SERVER_HOSTNAME = "127.0.0.1";
    public static final int DEFAULT_SERVER_PORT = 8000;
    public static final int DEFAULT_EVENT_LISTENER_PORT = 8001;
    
    public static final double DEFAULT_GENGRP_SNAPSHOT_QUALITY = 0.25;
    public static final int DEFAULT_GENGRP_SNAPSHOT_FREQUENCY = 5;
    public static final double DEFAULT_SPEGRP_SNAPSHOT_QUALITY = 0.50;
    public static final int DEFAULT_SPEGRP_SNAPSHOT_FREQUENCY = 3;
    public static final int DEFAULT_KEYSTROKE_FREQUENCY = 4;
    
    private static Config config;
    private File propertiesFile;
    private Properties properties;
    
    // Network-related properties.
    public final String serverHostname;
    public final int serverPort;
    public final int eventListenerPort;
    
    // Proctor-related properties.
    private double genGrpSnapshotQuality;
    private int genGrpSnapshotFrequency;
    private double speGrpSnapshotQuality;
    private int speGrpSnapshotFrequency;
    private int keystrokeFrequency;
    
    private Config() {
        propertiesFile = new File(CONFIG_FILENAME);
        properties = new Properties();
        
        // If the properties file does not exist, create one.
        if (!propertiesFile.exists()) {
            properties.setProperty("Server.hostname", DEFAULT_SERVER_HOSTNAME);
            properties.setProperty("Server.port", Integer.toString(DEFAULT_SERVER_PORT));
            properties.setProperty("EventListener.port", Integer.toString(DEFAULT_EVENT_LISTENER_PORT));
            
            properties.setProperty("Snapshot.genGrpQuality", Double.toString(DEFAULT_GENGRP_SNAPSHOT_QUALITY));
            properties.setProperty("Snapshot.genGrpFrequency", Integer.toString(DEFAULT_GENGRP_SNAPSHOT_FREQUENCY));
            properties.setProperty("Snapshot.speGrpQuality", Double.toString(DEFAULT_SPEGRP_SNAPSHOT_QUALITY));
            properties.setProperty("Snapshot.speGrpFrequency", Integer.toString(DEFAULT_SPEGRP_SNAPSHOT_FREQUENCY));
            properties.setProperty("Keystroke.patchReportFrequency", Integer.toString(DEFAULT_KEYSTROKE_FREQUENCY));
            
            save(propertiesFile);
        }
        
        // Load user configuration from the properties file.
        properties = load(propertiesFile);
        
        // Load critical network configuration.
        serverHostname = properties.getProperty("Server.hostname");
        serverPort = Integer.parseInt(properties.getProperty("Server.port"));
        eventListenerPort = Integer.parseInt(properties.getProperty("EventListener.port"));
        
        // Load proctor configuration.
        genGrpSnapshotQuality = Double.parseDouble(properties.getProperty("Snapshot.genGrpQuality"));
        genGrpSnapshotFrequency = Integer.parseInt(properties.getProperty("Snapshot.genGrpFrequency"));
        speGrpSnapshotQuality = Double.parseDouble(properties.getProperty("Snapshot.speGrpQuality"));
        speGrpSnapshotFrequency = Integer.parseInt(properties.getProperty("Snapshot.speGrpFrequency"));
        keystrokeFrequency = Integer.parseInt(properties.getProperty("Keystroke.patchReportFrequency"));
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
        properties.setProperty("Snapshot.genGrpQuality", Double.toString(genGrpSnapshotQuality));
        properties.setProperty("Snapshot.genGrpFrequency", Integer.toString(genGrpSnapshotFrequency));
        properties.setProperty("Snapshot.speGrpQuality", Double.toString(speGrpSnapshotQuality));
        properties.setProperty("Snapshot.speGrpFrequency", Integer.toString(speGrpSnapshotFrequency));
        properties.setProperty("Keystroke.patchReportFrequency", Integer.toString(keystrokeFrequency));
        
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
        Properties p = null;
        
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
    
    /**
     * Gets snapshot quality of the generic group.
     * @return snapshot quality of the generic group.
     */
    public double getGenGrpSnapshotQuality() {
        return genGrpSnapshotQuality;
    }
    
    /**
     * Gets snapshot frequency of the generic group.
     * @return snapshot frequency of the generic group.
     */
    public int getGenGrpSnapshotFrequency() {
        return genGrpSnapshotFrequency;
    }
    
    /**
     * Gets snapshot quality of the special group.
     * @return snapshot quality of the special group.
     */
    public double getSpeGrpSnapshotQuality() {
        return speGrpSnapshotQuality;
    }
    
    /**
     * Gets snapshot frequency of the special group.
     * @return snapshot frequency of the special group.
     */
    public int getSpeGrpSnapshotFrequency() {
        return speGrpSnapshotFrequency;
    }
    
    /**
     * Gets keystroke frequency.
     * @return keystroke frequency.
     */
    public int getKeystrokeFrequency() {
        return keystrokeFrequency;
    }
    
    /**
     * Updates all properties related to snapshot.
     * @param genGrpSnapshotQuality snapshot quality of the generic group.
     * @param genGrpSnapshotFrequency snapshot frequency of the generic group.
     * @param speGrpSnapshotQuality snapshot quality of the special group.
     * @param speGrpSnapshotFrequency snapshot frequency of the special group.
     * @param keystrokeFrequency keystroke frequency.
     */
    public void setSnapshotProperties(double genGrpSnapshotQuality, int genGrpSnapshotFrequency,
                                      double speGrpSnapshotQuality, int speGrpSnapshotFrequency) {
        this.genGrpSnapshotQuality = genGrpSnapshotQuality;
        this.genGrpSnapshotFrequency = genGrpSnapshotFrequency;
        this.speGrpSnapshotQuality = speGrpSnapshotQuality;
        this.speGrpSnapshotFrequency = speGrpSnapshotFrequency;
    }
    
    /**
     * Updates all properties related to keystroke.
     * @param keystrokeFrequency
     */
    public void setKeystrokeProperties(int keystrokeFrequency) {
        this.keystrokeFrequency = keystrokeFrequency;
    }
    
}