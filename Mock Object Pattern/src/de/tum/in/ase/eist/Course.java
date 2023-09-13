package de.tum.in.ase.eist;

public interface Course {

	boolean enroll(Student student);

	void drop();

	void start();
}
