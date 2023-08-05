package com.example.Group_project.COntroller;
//import
import com.example.Group_project.HElper.Messages;
import com.example.Group_project.REpository.*;
import com.example.Group_project.entities.*;
import com.example.Group_project.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
// import for message
import javax.servlet.http.HttpSession;
// import security file
import java.security.Principal;

// import
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")

//all view comes after /user
    public class UserCOntroller {

    // injecting UserRepository
        @Autowired// injecting password encoder
        private BCryptPasswordEncoder passwordEncoder;
        @Autowired// user
        private UserREpository userREpository;

        @Autowired// course
        private CourseREpository courseREpository;

        @Autowired// module repository
        private ModuleREpository moduleREpository;

        @Autowired// student
        private StudentREpository studentREpository;

        @Autowired
        private StaffREpository staffREpository;// staff repository

        @Autowired
// assignment db
        private AssignmentREpository assignmentREpository;


        // user/index handler that get to user_dashboard

        private int forModule = 0;
        private int forRegister = 0;

        @ModelAttribute
        public void addCommonData(Model model, Principal principal){
            // Model to add attribute. All attribute added is seen in user-dashboard html
            // Principal arrive from spring security. Unique identifyer of user is obtained from principal

            //quering through user name
            //get the user using username(email)
            //using userREpository to get from database
            String userName = principal.getName(); //obtaining user unique identifyer

            User user = userREpository.getUserByUserName(userName);
            // Logged in user information is obtained

            model.addAttribute("user", user);
            //sending user to html
        }

        @RequestMapping("/index")
        public String dashboard(Model model, Principal principal){
// return dashboard on request of user/index
            return "admin/user-dashboard";
        }

        @GetMapping("/add-course")// add course form
        public String openAddCourseForm(Model model){// add title
            model.addAttribute("title", "Add contact");//..
            model.addAttribute("course", new Course());// course return to html
// return--
            return "admin/add_course_form";


        }

        // processing add-course form
        @PostMapping("/process-course")
        // getting data from form by modelAttribute
        // matching field data are stored in database
        //if entities field match with form field then data are stored
        public String processCourse(@ModelAttribute Course course, Principal principal){
            String name = principal.getName();
            User user = this.userREpository.getUserByUserName(name);

            course.setUser(user);// giving user to course
            user.getCourses().add(course);// add course..
//.. save in user db
            this.userREpository.save(user);
            return "admin/add_course_form";//.. return
        }

        // show Course handler

        //current page = [0] page
        // show per page = [n=5]

        // {page} to show page no of pagination
        @GetMapping("/show-courses/{page}")
        public String showCourse(@PathVariable("page") Integer page, Model m, Principal principal){
            m.addAttribute("title", "Show user course");
// current user name
            String userName=  principal.getName();
            User user = this.userREpository.getUserByUserName(userName);//..

            // page request to pass page value
            // sends current page and size of course to show in page
            Pageable pageable = PageRequest.of(page, 3);
            // course list
            Page<Course> courses = this.courseREpository.findCourseByUser(user.getId(), pageable);

            //

            m.addAttribute("courses", courses); // all courses
            m.addAttribute("currentPage", page); // current page
            // total pages
            m.addAttribute("totalPages", courses.getTotalPages()); // total pages by dividing list of course
            return "admin/show_course";
        }

        // showing specific course details
        @RequestMapping("/{cId}/course")
        public String showCourseDetails(@PathVariable("cId") Integer cId, Model model){
            Optional<Course> courseOptional = this.courseREpository.findById(cId);///...
            Course course = courseOptional.get();//...
            // get and add to model
            model.addAttribute("course", course);
            // return model in that page
            return "admin/course_detail";
        }

        // delete contact handler
        @GetMapping("/delete/{cid}")
        public String deleteCourse(@PathVariable("cid") Integer cId, Model model, HttpSession session){
            // getting course from course repository
            Optional<Course> courseOptional = this.courseREpository.findById(cId);
            Course course = courseOptional.get();//--
//-- set null while delete
            course.setUser(null);
// to unlink and delete
            this.courseREpository.delete(course);

//            session.setAttribute("message", new Messages("course deleted successfully", "success"));
            return "redirect:/user/show-courses/0";
        }


// update form handler
    // PostMapping because if we only click on delete button then only it works
    @PostMapping("/update-course/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid, Model m){
            m.addAttribute("title", "update course");//..
            Course course = this.courseREpository.findById(cid).get();// get course
            m.addAttribute("course", course);//..
            return "admin/update_form";//--
    }

    // update course handler for processing form

    @PostMapping("/process-update")
    // Storing entity data by @ModelAttribute
    public String updateFormHandler(@ModelAttribute Course course,  Principal principal, HttpSession session){

            // getting current user id
            User user = this.userREpository.getUserByUserName(principal.getName());
            course.setUser(user);
            // setting user id
            this.courseREpository.save(course);
            session.setAttribute("message", new Messages("your course is updated.. ", "alert-success"));// show message
            return "redirect:/user/" + course.getcId() + "/course";// get course detail
    }



    // Getting add modules
   @GetMapping("/add-module")//
    private String openAddModuleForm(Model model){
       //..
            model.addAttribute("title", "Add Module");
            model.addAttribute("module", new Module());//..

            return "admin/add_module_form";
   }


   // processing add-module form
    @PostMapping("/process-module")
//..
    public String processModule(@ModelAttribute Module module, HttpSession session) {
//...
        module.setCourses(this.courseREpository.getReferenceById(forModule));
        this.moduleREpository.save(module);//.. save in database
// set successful message
        session.setAttribute("message", new Messages("successfully added", "alert-success"));
        return "redirect:/user/show-modules/" + String.valueOf(forModule);// return
    }
//'
    @GetMapping("/show-modules/{cid}")//---
    public String showCourse(@PathVariable("cid") Integer cId, Model m){
        forModule = cId;// set as global
        m.addAttribute("title", "Show Modules of courses");// to
        List<Module> modules = moduleREpository.findModuleByCourse(cId);// list the modules
        m.addAttribute("modules", modules);//-
        return "admin/show_module";//ok return
    }
// update module
    @PostMapping("/update-module/{mid}")//..
    public String updateModuleForm(@PathVariable("mid") Integer mid, Model m){
        m.addAttribute("title", "update Module");// add title
        Module module = this.moduleREpository.findById(mid).get();// get
        m.addAttribute("module", module);//..
        return "admin/update_module_form";//..
    }
// process update
    @PostMapping("/process-module-update")
    // Storing entity data by @ModelAttribute
    public String updateModuleFormHandler(@ModelAttribute Module module, HttpSession session){

        // getting current course id
        Course course = this.courseREpository.findById(forModule).get();
        module.setCourses(course);// set course for module
        // setting course id
        this.moduleREpository.save(module);
        return "redirect:/user/show-modules/" + course.getcId();// return to
    }

    @GetMapping("/delete-module/{mid}")
    public String deleteModule(@PathVariable("mid") Integer mId, Model model, HttpSession session){
        // getting course from course repository
        Optional<Module> moduleOptional = this.moduleREpository.findById(mId);//..
        Module module = moduleOptional.get();// get

        module.setCourses(null);
// delete module
        this.moduleREpository.delete(module);//
       session.setAttribute("message", new Messages("module deleted successfully", "alert-success"));//..
        return "redirect:/user/show-modules/" + String.valueOf(forModule);//...
    }


    // students
// add
    @GetMapping("/add-student")
    private String openAddStudentForm(Model model){
        List<Course> options = courseREpository.findAll();// find all
        model.addAttribute("title", "Add Student");//..
        model.addAttribute("student", new Student());//...
        model.addAttribute("options", options);//..
        return "admin/add_student_form";//..
    }
// process add student
    @PostMapping("/process-student")
    public String processStudent(@ModelAttribute Student student, @RequestParam(value = "courses") Course course, HttpSession session) {//..
        User user  = new User();// new user
        user.setName(student.getStudentName());//...
        user.setEmail(student.getEmail());//...
        user.setRole("ROLE_STUDENT");//" "
        user.setEnabled(true);// ture
        user.setImageUrl("default.png");//...
        user.setPassword(passwordEncoder.encode(student.getStudentName()));//...
        user.setAbout("student user");//about
        student.setStatus("REGISTERED");//'
        student.setCourse(course);//---
        userREpository.save(user);// save
        this.studentREpository.save(student);//--

//        session.setAttribute("message", new Messages("successfully added", "alert-success"));
        return "redirect:/user/show-students";
    }
// show students
    @GetMapping("/show-students")
    public String showStudents(Model m){
        m.addAttribute("title", "List of student");//..
        List<Student> students  = this.studentREpository.findAll();//..
        m.addAttribute("students", students);//:
        return "admin/show_student";//" "

    }

//delete student with id
    @GetMapping("/delete-student/{sid}")//delete student
    public String deleteStudent(@PathVariable("sid") Integer sId, Model model, HttpSession session){
        // getting course from course repository
        Optional<Student> studentOptional = this.studentREpository.findById(sId);// get
        Student student = studentOptional.get();//..
        student.setCourse(null);//.. set null to delete child table
        this.studentREpository.delete(student);//..
        session.setAttribute("message", new Messages("Student deleted successfully", "alert-success"));//...
        return "redirect:/user/show-students" ;//...
    }
//...
    @PostMapping("/update-student/{sid}")
    public String updateStudentForm(@PathVariable("sid") Integer sid, Model m){//...
        List<Course> options = courseREpository.findAll();//...
        m.addAttribute("title", "update student");//...
        Student student = this.studentREpository.findById(sid).get();// find by id
        m.addAttribute("student", student);// student object
        m.addAttribute("options", options);//..
        return "admin/update_student_form";// return
    }

    @PostMapping("/process-student-update")
    // Storing entity data by @ModelAttribute
    public String updatestudentFormHandler(@ModelAttribute Student student, @RequestParam(value = "courses") Course course, HttpSession session){

//        // getting current course id
//        Course course = this.courseREpository.findById(forModule).get();
//        module.setCourses(course);
        // setting course id
        student.setCourse(course);
        this.studentREpository.save(student);// save student
        session.setAttribute("message", new Messages("Student Updated successfully", "alert-success"));// message
        return "redirect:/user/show-students";//..
    }
// show staffs
    @GetMapping("/show-staffs")//..
    public String showStaffs( Model m){// add title
        m.addAttribute("title", "List of Staffs");
        List<Staff> staffs  = this.staffREpository.findAll();// list of staff
        m.addAttribute("staffs", staffs);//return staff
        return "admin/show_staff";
    }
// for add staff
    @GetMapping("/add-staff")
    private String openAddStaffForm(Model model){
        List<Module> options = moduleREpository.findAll();// get options
        model.addAttribute("title", "Add Staff");//..
        model.addAttribute("staff", new Staff());//..
        model.addAttribute("options", options);//...
        return "admin/add_staff_form";//...
    }

    @PostMapping("/process-staff")
    public String processStaff(@ModelAttribute Staff staff, @RequestParam(value = "modules") Module module, HttpSession session) {
// process add staff
        User user  = new User();//..
        user.setName(staff.getStaffName());//..
        user.setEmail(staff.getEmail());//...
        user.setRole("ROLE_STAFF");//.. set for login
        user.setEnabled(true);//...
        user.setImageUrl("default.png");//....
        user.setPassword(passwordEncoder.encode(staff.getStaffName()));// set encoded pass
        user.setAbout("Staff user");//...
        userREpository.save(user);//..
        staff.setModules(module);//.. set
        this.staffREpository.save(staff);// ok
        session.setAttribute("message", new Messages("Staff added Successfully", "alert-success"));//ok
        return "redirect:/user/show-staffs";// return
    }
// delete staff
    @GetMapping("/delete-staff/{staffid}")//..
    public String deleteStaff(@PathVariable("staffid") Integer sId, Model model, HttpSession session){
        // getting course from course repository
        Optional<Staff> staffOptional = this.staffREpository.findById(sId);//..
        Staff staff = staffOptional.get();// get staff
        staff.setModules(null);// set module of staff
        this.staffREpository.delete(staff);// delete
        session.setAttribute("message", new Messages("Staff deleted successfully", "alert-success"));//..
        return "redirect:/user/show-staffs" ;//..
    }
// update staff with id
    @PostMapping("/update-staff/{sid}")//..
    public String updateStaffForm(@PathVariable("sid") Integer sid, Model m){
        List<Module> options = moduleREpository.findAll();// list of modules
        m.addAttribute("title", "update staff");//..
        Staff staff = this.staffREpository.findById(sid).get();//..
        m.addAttribute("staff", staff);//...
        m.addAttribute("options", options);//return options
        return "admin/update_staff_form";//..
    }

    @PostMapping("/process-staff-update")
    // Storing entity data by @ModelAttribute
    public String updatestaffFormHandler(@ModelAttribute Staff staff, @ModelAttribute("modules") Module module, HttpSession session){
        staff.setModules(module);// set module in staff
        this.staffREpository.save(staff);//
        session.setAttribute("message", new Messages("successfully updated", "alert-success"));//...

        return "redirect:/user/show-staffs";//....
    }
// add assignment
    @GetMapping("/add-assignment")
    private String openAddAssignmentForm(Model model){//...
        List<Module> options = moduleREpository.findAll();
        model.addAttribute("title", "Add assignment");// title
        model.addAttribute("text", "ADD ASSIGNMENT");//text
        model.addAttribute("staff", new Staff());//..
        model.addAttribute("options", options);//...
        return "admin/add_assignment";// return
    }
// show assignment
    @GetMapping("/show-assignment")
    public String showAssignment( Model m){// function
        m.addAttribute("title", "List of Assignments");//..
        List<Assignment> assignments  = this.assignmentREpository.findAll();//....
        m.addAttribute("assignment", assignments);//return assignment
        return "admin/show_assignment";//..
    }
// process add assignment
    @PostMapping("/process-assignment")// ask for assignment object
    public String processAssignment(@ModelAttribute Assignment assignment, @RequestParam(value = "modules") Module module, HttpSession session) {
       assignment.setModule(module);// set..
       this.assignmentREpository.save(assignment);//
        session.setAttribute("message", new Messages("Assignment added Successfully", "alert-success"));//..
        return "redirect:/user/show-assignment";//.
    }
// delete assignment
    @GetMapping("/delete-assignment/{aid}")
    public String deleteAssignment(@PathVariable("aid") Integer aId, Model model, HttpSession session){//delete for
// assignment
        Optional<Assignment> asOptional = this.assignmentREpository.findById(aId);
        Assignment assignment = asOptional.get();// get assignment to delete
        assignment.setModule(null);// set null to module
        this.assignmentREpository.delete(assignment);// save in that assignment
        session.setAttribute("message", new Messages("Assignment deleted successfully", "alert-success"));//..
        return "redirect:/user/show-assignment" ;// return
    }
// update assignment
    @PostMapping("/update-assignment/{aid}")//..
    public String updateAssignmentForm(@PathVariable("aid") Integer aid, Model m){//:
        List<Module> options = moduleREpository.findAll();// find all modules
        m.addAttribute("title", "update Assignment");// return title
        Assignment assignment = this.assignmentREpository.findById(aid).get();//...
        m.addAttribute("assignment", assignment);//...
        m.addAttribute("options", options);//return dropdown
        return "admin/update_assignment_form";//return
    }

    @PostMapping("/process-assignment-update")
    // Storing entity data by @ModelAttribute
    public String updateAssignmentFormHandler(@ModelAttribute Assignment assignment, @ModelAttribute("modules") Module module, HttpSession session){
        assignment.setModule(module);//set module
        this.assignmentREpository.save(assignment);// save in db
        session.setAttribute("message", new Messages("successfully updated", "alert-success"));//..

        return "redirect:/user/show-assignment";//..redirect
    }
}








