package org.iman.Heimdallr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/")
public class LoginController {

    @PostMapping("/login/account")
    public String checkAccount(@RequestBody ObjectNode req) {
        System.out.println(req.toString());
        String resp = "{\"status\":\"ok\",\"type\":\"account\",\"currentAuthority\":\"admin\"}";
        return resp;
    }
    
    @GetMapping("/currentUser")
    public String getCurrentUser() {
        System.out.println("currentUser: " );
        String resp = "{\n" + 
                "    \"name\": \"Serati Ma\",\n" + 
                "    \"avatar\": \"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png\",\n" + 
                "    \"userid\": \"00000001\",\n" + 
                "    \"email\": \"antdesign@alipay.com\",\n" + 
                "    \"signature\": \"海纳百川，有容乃大\",\n" + 
                "    \"title\": \"交互专家\",\n" + 
                "    \"group\": \"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED\",\n" + 
                "    \"tags\": [{\n" + 
                "            \"key\": \"0\",\n" + 
                "            \"label\": \"很有想法的\",\n" + 
                "        },\n" + 
                "        {\n" + 
                "            \"key\": \"1\",\n" + 
                "            \"label\": \"专注设计\",\n" + 
                "        },\n" + 
                "    ],\n" + 
                "    \"notifyCount\": 12,\n" + 
                "    \"unreadCount\": 11,\n" + 
                "    \"country\": \"China\",\n" + 
                "    \"access\": \"admin\",\n" + 
                "    \"geographic\": {\n" + 
                "        \"province\": {\n" + 
                "            \"label\": \"浙江省\",\n" + 
                "            \"key\": \"330000\",\n" + 
                "        },\n" + 
                "        \"city: {\n" + 
                "        \"label\": \"杭州市\",\n" + 
                "        \"key\": \"330100\",\n" + 
                "    },\n" + 
                "},\n" + 
                "\"address\": \"西湖区工专路 77 号\",\n" + 
                "\"phone\": \"0752-268888888\",\n" + 
                "}";
        return resp;
    }
    
    @GetMapping("/notices")
    public String getNotices() {
        System.out.println("Get notices ==>" );
        String resp = "{\n" + 
                "    data: [\n" + 
                "      {\n" + 
                "        id: '000000001',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',\n" + 
                "        title: '你收到了 14 份新周报',\n" + 
                "        datetime: '2017-08-09',\n" + 
                "        type: 'notification',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000002',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/OKJXDXrmkNshAMvwtvhu.png',\n" + 
                "        title: '你推荐的 曲妮妮 已通过第三轮面试',\n" + 
                "        datetime: '2017-08-08',\n" + 
                "        type: 'notification',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000003',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/kISTdvpyTAhtGxpovNWd.png',\n" + 
                "        title: '这种模板可以区分多种通知类型',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        read: true,\n" + 
                "        type: 'notification',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000004',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png',\n" + 
                "        title: '左侧图标用于区分不同的类型',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        type: 'notification',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000005',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png',\n" + 
                "        title: '内容不要超过两行字，超出时自动截断',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        type: 'notification',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000006',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',\n" + 
                "        title: '曲丽丽 评论了你',\n" + 
                "        description: '描述信息描述信息描述信息',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        type: 'message',\n" + 
                "        clickClose: true,\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000007',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',\n" + 
                "        title: '朱偏右 回复了你',\n" + 
                "        description: '这种模板用于提醒谁与你发生了互动，左侧放『谁』的头像',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        type: 'message',\n" + 
                "        clickClose: true,\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000008',\n" + 
                "        avatar: 'https://gw.alipayobjects.com/zos/rmsportal/fcHMVNCjPOsbUGdEduuv.jpeg',\n" + 
                "        title: '标题',\n" + 
                "        description: '这种模板用于提醒谁与你发生了互动，左侧放『谁』的头像',\n" + 
                "        datetime: '2017-08-07',\n" + 
                "        type: 'message',\n" + 
                "        clickClose: true,\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000009',\n" + 
                "        title: '任务名称',\n" + 
                "        description: '任务需要在 2017-01-12 20:00 前启动',\n" + 
                "        extra: '未开始',\n" + 
                "        status: 'todo',\n" + 
                "        type: 'event',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000010',\n" + 
                "        title: '第三方紧急代码变更',\n" + 
                "        description: '冠霖提交于 2017-01-06，需在 2017-01-07 前完成代码变更任务',\n" + 
                "        extra: '马上到期',\n" + 
                "        status: 'urgent',\n" + 
                "        type: 'event',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000011',\n" + 
                "        title: '信息安全考试',\n" + 
                "        description: '指派竹尔于 2017-01-09 前完成更新并发布',\n" + 
                "        extra: '已耗时 8 天',\n" + 
                "        status: 'doing',\n" + 
                "        type: 'event',\n" + 
                "      },\n" + 
                "      {\n" + 
                "        id: '000000012',\n" + 
                "        title: 'ABCD 版本发布',\n" + 
                "        description: '冠霖提交于 2017-01-06，需在 2017-01-07 前完成代码变更任务',\n" + 
                "        extra: '进行中',\n" + 
                "        status: 'processing',\n" + 
                "        type: 'event',\n" + 
                "      },\n" + 
                "    ],\n" + 
                "  }";
        return resp;
    }
}
