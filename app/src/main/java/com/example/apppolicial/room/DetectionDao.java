package com.example.apppolicial.room;


import androidx.room.*;

import java.util.List;

@Dao
public interface DetectionDao {

	@Query("SELECT * FROM detections")
	List<Detection> getAll();

	@Query("DELETE FROM detections")
	void deleteAll();

	@Query("SELECT * FROM detections WHERE id IN (:dataIds)")
	List<Detection> findByIds(int[] dataIds);

	@Query("SELECT * FROM detections WHERE nome LIKE :nomeQ")
	List<Detection> findByName(String nomeQ);



	@Insert
	void insertAll(Detection... detections);

	@Delete
	void delete(Detection detection);



}
