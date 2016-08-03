package com.turborep.turbotracker.settings.service;

import java.util.List;

import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

public interface ShipViaService {

	public List<Veshipvia> getVeShipVia() throws UserException;

	public Veshipvia updateShipViaDetails(Veshipvia theVeshipvia) throws UserException;

	public Boolean deleteveshipdetail(Integer aveShipViaId) throws UserException;

	public Veshipvia addShipViaDetails(Veshipvia aVeshipvia) throws UserException;

}
