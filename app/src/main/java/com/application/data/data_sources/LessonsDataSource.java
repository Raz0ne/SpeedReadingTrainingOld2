package com.application.data.data_sources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import com.application.data.models.Lesson;

public class LessonsDataSource {
    public LiveData<List<Lesson>> lessons() {
        MutableLiveData<List<Lesson>> result = new MutableLiveData<>();

        return result;
    }
}
