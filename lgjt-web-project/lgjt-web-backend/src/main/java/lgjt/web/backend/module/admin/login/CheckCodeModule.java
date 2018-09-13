package lgjt.web.backend.module.admin.login;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.utils.StaticUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:后端登录验证码
 */

@At("/checkcode")
@IocBean
@Log4j
public class CheckCodeModule {

    /**
     *
     * @throws IOException 抛出IO流异常
     */
    @Ok("raw:stream")
    @At("/getCheckCode")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public void getCheckCode(String token) throws IOException {
        HttpServletResponse response = Mvcs.getResp();
        HttpServletRequest request = Mvcs.getReq();
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setContentType("image/jpeg");

        // 通过程序创建图片
        createCheckCode(request, response,token);

    }

    public void createCheckCode(HttpServletRequest request,
                                HttpServletResponse response,String token) throws IOException {

        //1.生成随机字符串
        //声明一个随机字符串
        String baseString ="ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
        //生成一个随机数
        Random rando= new Random();
        //新建一个容量为4的数组
        char[] randomCharArr = new char[4];
        //以随机数为下标读取字符串4个字符，并依次赋给数组
        for (int i=0;i<randomCharArr.length;i++){
            //toCharArray()将此字符串转换为一个新的字符数组
            //nextInt(int n)  返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）和指定值（不包括）之间均匀分布的 int 值。
            //rando.nextInt(baseString.length())的意思是在声明的字符串下标的范围内生成随机数
            randomCharArr[i] = baseString.toCharArray()[rando.nextInt(baseString.length())];
        }

        //2.新建一个图片
        //为字符串准备一个画板
        int width = 120;
        int height = 30;
        //在内存中新建一个画板       BufferedImage描述具有可访问图像数据缓冲区的 Image
        BufferedImage bi= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //新建一个画笔(为组件创建一个图形上下文)
        Graphics g = bi.getGraphics();
        //为背景填充一个白色
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        //为图片画一个边框
        g.setColor(Color.black);
        g.drawRect(1, 1, width-2, height-2);

        //3.把随机数添加到画板上
        // 设置字体
        g.setFont(new Font("宋体", Font.ITALIC|Font.BOLD, 30));

        g.setColor(Color.red);
        for(int i=0;i<randomCharArr.length;i++){
            g.drawString(String.valueOf(randomCharArr[i]), i*20+10, 25);
        }
        //增加干扰因素
        g.setColor(Color.black);
        int x1,y1,x2,y2;
        for(int i=0;i<5;i++){
            x1 = rando.nextInt(width);
            y1 = rando.nextInt(height);
            x2 = rando.nextInt(width);
            y2 = rando.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        //4.保存备份以作对比
        //把这个字符保存到session中
        IObjectCache cache = CacheFactory.getObjectCache();
        cache.set("CODE:"+token, new String(randomCharArr));
        cache.setExpireTime("CODE:"+token, 5*60);//验证码5分钟失效
        //5.把图片发送给浏览器
        response.setContentType("image/jpeg");//告诉浏览器这是一个图片
        response.setHeader(StaticUtils.TOKEN_NAME, token);
        ImageIO.write(bi, "jpg", response.getOutputStream());//把内存当中的图片通过流的形式发送给浏览器
    }


}
