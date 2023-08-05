package com.example.Group_project.COntroller;
// importing
import com.example.Group_project.REpository.*;
//import
import com.example.Group_project.entities.Module;
import com.example.Group_project.entities.Staff;
import com.example.Group_project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
// import
import java.security.Principal;
import java.util.List;
import java.util.Optional;

// controller to handle request
@Controller
@RequestMapping("/staff")
public class StaffCOntroller {
// user repository
        @Autowired
        private UserREpository userREpository;

        @Autowired
        private CourseREpository courseREpository;// course related db

        @Autowired
        private StudentREpository studentREpository;// student

        @Autowired
        private StaffREpository staffREpository;// staff

        @Autowired
        private ModuleREpository moduleREpository;// module

// adding common data like username throught the code.
        @ModelAttribute
        public void addCommonData(Model model, Principal principal){

            String userName = principal.getName();// string
            User user = userREpository.getUserByUserName(userName);// user object
            model.addAttribute("user", user);

        }

        @RequestMapping("/index")// return dashboard on request from staff login
        public String homepageView(Model model, Principal principal){
            return "staff/staff-dashboard";
        }

        /* @RequestMapping("/show-courses")
    public String showCourse(Model m, Principal principal){
//        m.addAttribute("title", "Student courses");
        String userName =  principal.getName();
        Student student = this.studentREpository.getStudentByUserName(userName);
        List<Course> courses = this.courseREpository.findCourseById(student.getCourse().getcId());
        m.addAttribute("courses", courses);
        return "student/show-course";
    }*/
        @GetMapping("/show-modules")// request for show module
        public String showModule(Model m, Principal principal){
        String userName = principal.getName();//
        Staff staff = this.staffREpository.getStaffByUserName(userName);
        m.addAttribute("title", "Staff enrolled Modules");// set title
        List<Module> modules = this.moduleREpository.findModuleById(staff.getModules().getmId());// list of objects
        m.addAttribute("modules", modules);// add that list and return
        return "staff/show_module";// return show_module page
         }

         // to show modules of staff
        @RequestMapping("/{mId}/module/")// request
        public String showModuleDetails(@PathVariable("mId") Integer mId, Model model){// function when requested
        Optional<Module> optionalModule = this.moduleREpository.findById(mId);// optional value can consists of null
        Module module = optionalModule.get();
        model.addAttribute("module", module);// return module object
        return "staff/module_detail";// return that page
    }

//


}
