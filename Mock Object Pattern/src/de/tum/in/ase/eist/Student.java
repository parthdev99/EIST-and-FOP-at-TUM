package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private final List<Course> courses = new ArrayList<>();

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
	}
}
