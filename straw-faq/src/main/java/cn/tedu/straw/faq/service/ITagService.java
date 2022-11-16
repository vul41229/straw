package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2022-09-22
 */
public interface ITagService extends IService<Tag> {

    List<Tag> getTags();

    Map<String,Tag> getTagMap();
}
