package com.example.Group_project.COntroller;
//import
import com.example.Group_project.HElper.Messages;
import com.example.Group_project.REpository.*;
import com.example.Group_project.entities.*;
import com.example.Group_project.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
//import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

// controller for student request
@Controller
@RequestMapping("/student")
public class StudentCOntroller {

    @Autowired
    private UserREpository userREpository;// user db

    @Autowired
    private CourseREpository courseREpository;// calling course db

    @Autowired
    private ModuleREpository moduleREpository;// module related db
    @Autowired
    private StudentREpository studentREpository;// student db

    @Autowired// assignment db
    private AssignmentREpository assignmentREpository;

    // for encrypting new password
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;// password encoder for student login

    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
// adding common data
        String userName = principal.getName();
        User user = userREpository.getUserByUserName(userName);// in user object
        model.addAttribute("user", user);

    }
// request for student/index
    @RequestMapping("/index")
    public String homepageView(Model model, Principal principal){
        return "student/student-dashboard";
    }
// request for show-courses
    @RequestMapping("/show-courses")
    public String showCourse(Model m, Principal principal){// principal means correct logged in user
//        m.addAttribute("title", "Student courses");
        String userName =  principal.getName();
        Student student = this.studentREpository.getStudentByUserName(userName);// student
        List<Course> courses = this.courseREpository.findCourseById(student.getCourse().getcId());// get course from student enrolled course
        m.addAttribute("courses", courses);// add courses and return
        return "student/show-course";// return in show-courses
    }

    @GetMapping("/show-modules/{cid}")// request for show modules
    public String showModule(@PathVariable("cid") Integer cId, Model m){//
        m.addAttribute("title", "Show Modules of courses");//..
        List<Module> modules = moduleREpository.findModuleByCourse(cId);// List of modules
        m.addAttribute("modules", modules);//...
        return "student/show_module";// return
    }

    @RequestMapping("/{mId}/module/")// request for particular module
    public String showModuleDetails(@PathVariable("mId") Integer mId, Model model){//...
        Optional<Module> optionalModule = this.moduleREpository.findById(mId);// optional value
        Module module = optionalModule.get();// get that module
        model.addAttribute("module", module);//add
        return "student/module_detail";//...
    }

    @GetMapping("/show-assignment/{mid}")//....
    public String showAssignment(@PathVariable("mid") Integer mId, Model m){//:
        m.addAttribute("title", "Show Assignment of modules");//.....
        List<Assignment> assignments = assignmentREpository.findAssignmentByModule(mId);// list of assignment
        // add in model and return
        m.addAttribute("assignment", assignments);//...
        return "student/show_assignment";// return..
    }

    // open change password handler
    @GetMapping("/change-password")
    public String changePassword(Model model) {
       return "student/change_password";
    }

    // process change password
    @PostMapping("/process-password")// request newPassword as input
    public String changePassword(@RequestParam("newPassword") String newPass, @RequestParam("oldPassword") String oldPass, Principal principal, HttpSession session) {
// get that user
        String username = principal.getName();
        User user1 = this.userREpository.getUserByUserName(username);//..

//        // if user input old password matches with password stored in database
        if(this.bCryptPasswordEncoder.matches(oldPass, user1.getPassword())){
            // change password
            user1.setPassword(this.bCryptPasswordEncoder.encode(newPass));
            this.userREpository.save(user1);// save in db
            session.setAttribute("message", new Messages("Your password successfully changed.", "alert-success"));//.. show successful message
        }
        else {
//            // if error
            session.setAttribute("message", new Messages("Please enter correct old message.", "alert-danger"));//.. show this
            return "redirect:/student/change-password";//..return to page
        }
//return
        return "redirect:/student/index";// to index
    }

}
