package com.silversnowsoftware.vc.data.db;

import com.silversnowsoftware.vc.model.FileModel;
import com.silversnowsoftware.vc.utils.enums.FileStatusEnum;

import java.util.List;

/**
 * Created by burak on 10/22/2018.
 */


public interface IRepository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void removeAll();

    int removeIds(List<Integer> ids);

    boolean removeItems(List<T> items);

    T getById(Integer id);

    List<T> getAll();

    T getByObject(T item);

    boolean exist(Integer id);

    public List<FileModel> getFileModelListWithFileStatus(String field,FileStatusEnum value);
}