package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Category;

public interface CategoryService {

	Category saveCategory(Category category);

	Category getCategoryById(Long id);

	Category editCategory(Category category);

	void deleteCategoryById(Long id);

	List<Category> getAllCategories();
}
