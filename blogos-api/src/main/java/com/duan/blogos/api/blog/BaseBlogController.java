package com.duan.blogos.api.blog;

import com.duan.blogos.annonation.TokenNotRequired;
import com.duan.blogos.api.BaseCheckController;

/**
 * Created on 2017/12/26.
 * 博主无需登录即可获取
 *
 * @author DuanJiaNing
 */
@TokenNotRequired
public class BaseBlogController extends BaseCheckController {

}
