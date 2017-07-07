package org.educama.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The MVC controller of csv upload view.
 */
@Controller
public class MapperController {

    @RequestMapping("/csvUpload")
    public String uploadCsv(Model model) {
        model.addAttribute("message", "message");
        return "csvUpload";
    }
}
