package com.golecdriver;

import java.io.Serializable;

public class TripData implements Serializable {
    String date,tmie,pickLo,dropLo,driverName,driverId,clientName,clientId,startAddress,endAddress,endlogs,km;

    public TripData(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTmie() {
        return tmie;
    }

    public void setTmie(String tmie) {
        this.tmie = tmie;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getPickLo() {
        return pickLo;
    }

    public void setPickLo(String pickLo) {
        this.pickLo = pickLo;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndlogs() {
        return endlogs;
    }

    public void setEndlogs(String endlogs) {
        this.endlogs = endlogs;
    }

    public String getDropLo() {
        return dropLo;
    }

    public void setDropLo(String dropLo) {
        this.dropLo = dropLo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
