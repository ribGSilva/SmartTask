package com.projects.gabriel.smarttask.app;

import android.app.Application;

import com.projects.gabriel.smarttask.domain.entities.Client;

public class SmartTaskApplication extends Application {

    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}