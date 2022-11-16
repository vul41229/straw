package cn.tedu.straw.faq.controller;



import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.faq.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
@RestController
@RequestMapping("/v2/tags")
public class TagController {
    @Autowired
    ITagService tagService;
    @GetMapping("")
    public List<Tag> tags(){
        List<Tag> tags = tagService.getTags();
        return tags;
    }
}
