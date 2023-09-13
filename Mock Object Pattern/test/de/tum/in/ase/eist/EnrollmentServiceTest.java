package de.tum.in.ase.eist;

import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(EasyMockExtension.class)
class EnrollmentServiceTest {
    @TestSubject
    private EnrollmentService enrollmentService = new EnrollmentService();

    @Mock
    private Course courseMock;

    // TODO 1: setup EnrollmentServiceTest with all necessary attributes

    @Test
    void testEnrollStudentSuccessful() {

        // TODO 2: implement the test
        Student student = new Student();
        int expectCount = student.getCourses().size() + 1;

        expect(courseMock.enroll(student)).andReturn(true);

        replay(courseMock);

        enrollmentService.enroll(student, courseMock);
        assertEquals(expectCount, student.getCourses().size());
    }

    @Test
    void testEnrollStudentFailure() {
        Student student = new Student();
        int expectCount = student.getCourses().size();

        expect(courseMock.enroll(student)).andReturn(false);
        expect(courseMock.enroll(student)).andReturn(false).atLeastOnce();

        replay(courseMock);

        enrollmentService.enroll(student, courseMock);
        assertEquals(expectCount, student.getCourses().size());

        // TODO 3: Implement the test (optional)
    }

}
