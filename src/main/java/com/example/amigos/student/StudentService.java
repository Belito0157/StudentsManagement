package com.example.amigos.student;

import com.example.amigos.student.exception.BadRequestException;
import com.example.amigos.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }


    public void addStudent(Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if(existsEmail){
            throw new BadRequestException("Email is already taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new StudentNotFoundException("student with this id does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentid, String name, String email, Gender gender){
        Student student = studentRepository.findById(studentid).orElseThrow(() -> new IllegalStateException(
                "student does not exist"
        ));

        if(name!= null&&name.length()>0&&!Objects.equals(student.getName(), name)){
            student.setName(name);
        }
        if(email!=null&&email.length()>0){
            student.setEmail(email);
        }
        if(gender!=null&&email.length()>0){
            student.setGender(gender);
        }
    }
}
