package com.sajedhamdan.taskmanager.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sajedhamdan.taskmanager.dto.*;
import com.sajedhamdan.taskmanager.entity.Task;
import com.sajedhamdan.taskmanager.exception.ResourceNotFoundException;
import com.sajedhamdan.taskmanager.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;

	public TaskResponse createTask(TaskRequest request) {
		Task task = new Task();
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setDueDate(request.getDueDate());
		task.setCompleted(request.isCompleted());

		Task saved = repository.save(task);
		return mapToResponse(saved);
	}

	public List<TaskResponse> getAllTasks() {
		List<Task> tasks = repository.findAll();
		return tasks.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public TaskResponse updateTask(UUID id, TaskRequest request) {
		Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));

		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setDueDate(request.getDueDate());
		task.setCompleted(request.isCompleted());

		Task updated = repository.save(task);
		return mapToResponse(updated);
	}

	public void deleteTask(UUID id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Task with id " + id + " not found");
		}
		repository.deleteById(id);
	}

	private TaskResponse mapToResponse(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setDueDate(task.getDueDate());
		response.setCompleted(task.isCompleted());
		return response;
	}
}