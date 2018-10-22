package com.silversnowsoftware.vc.data.db;

import com.silversnowsoftware.vc.model.FileModel;

import java.util.List;

/**
 * Created by burak on 10/22/2018.
 */


public interface IRepository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    T getbyId(Integer id);
    List<T> getAll();
}