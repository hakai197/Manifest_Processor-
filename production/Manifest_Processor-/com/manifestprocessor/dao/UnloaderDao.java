package com.techelevator.custom.dao;

import com.techelevator.custom.model.Unloader;

import java.util.List;

public interface UnloaderDao {
    Unloader getUnloaderById(int id);

    List<Unloader> getUnloaders();

    Unloader createUnloader(Unloader unloader);

    Unloader updateUnloader(Unloader unloader);

    int deleteUnloaderById(int id);

    List<Unloader> getAllUnloaders();
}
