package org.example.cache.configs;

import org.springframework.stereotype.Component;

@Component
public class AspectsLoggersConfig {

    private boolean isLogsAroundMethodsEnabled = true;
    private boolean isRepoLoggingEnabled = true;
    private boolean isCacheLoggingEnabled = true;
    private boolean isServiceLoggingEnabled = true;

    public void enableAll() {
        this.isLogsAroundMethodsEnabled = true;
        this.isRepoLoggingEnabled = true;
        this.isCacheLoggingEnabled = true;
        this.isServiceLoggingEnabled = true;
    }

    public void disableAll() {
        this.isLogsAroundMethodsEnabled = false;
        this.isRepoLoggingEnabled = false;
        this.isCacheLoggingEnabled = false;
        this.isServiceLoggingEnabled = false;
    }

    public void enableLogsAroundMethods() {
        this.isLogsAroundMethodsEnabled = true;
    }

    public void enableRepoLogging() {
        this.isRepoLoggingEnabled = true;
    }

    public void enableCacheLogging() {
        this.isCacheLoggingEnabled = true;
    }

    public void enableServiceLogging() {
        this.isServiceLoggingEnabled = true;
    }

    public void disableLogsAroundMethods() {
        this.isLogsAroundMethodsEnabled = false;
    }

    public void disableRepoLogging() {
        this.isRepoLoggingEnabled = false;
    }

    public void disableCacheLogging() {
        this.isCacheLoggingEnabled = false;
    }

    public void disableServiceLogging() {
        this.isServiceLoggingEnabled = false;
    }

    public boolean isLogsAroundMethodsEnabled() {
        return isLogsAroundMethodsEnabled;
    }

    public boolean isRepoLoggingEnabled() {
        return isRepoLoggingEnabled;
    }

    public boolean isCacheLoggingEnabled() {
        return isCacheLoggingEnabled;
    }

    public boolean isServiceLoggingEnabled() {
        return isServiceLoggingEnabled;
    }

}
