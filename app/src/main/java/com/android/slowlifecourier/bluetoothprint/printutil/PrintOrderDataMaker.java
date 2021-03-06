package com.android.slowlifecourier.bluetoothprint.printutil;

import android.content.Context;

import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.android.slowlifecourier.objectmodle.PrintOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 测试数据生成器
 * Created by liuguirong on 8/1/17.
 */

public class PrintOrderDataMaker implements PrintDataMaker {

    private String qr;
    private int width;
    private int height;
    Context btService;
    private PrintOrder orderEntity;

    public PrintOrderDataMaker(Context btService, String qr, int width, int height, PrintOrder orderEntity) {
        this.qr = qr;
        this.width = width;
        this.height = height;
        this.btService = btService;
        this.orderEntity=orderEntity;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();

        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width)
                    : new PrinterWriter80mm(height, width);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
//            打印图片
//            ArrayList<byte[]> image1 = printer.getImageByte(btService.getResources(), R.drawable.hear3);
//
//            data.addAll(image1);
//            printer.setAlignLeft();
//            printer.printLine();
//            printer.printLineFeed();

//            printer.printLineFeed();
//            printer.setAlignCenter();
//            printer.setEmphasizedOn();
            printer.setFontSize(1);
//            printer.print(orderEntity.getReceiverName());
//            printer.printLineFeed();
//            printer.setEmphasizedOff();
//            printer.printLineFeed();
//
//            printer.printLineFeed();
//            printer.setFontSize(0);
//            printer.setAlignCenter();
//            printer.print("订单编号：" + orderEntity.getOrderNumber());
//            printer.printLineFeed();

//            printer.setAlignCenter();
//            printer.print(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
//                    .format(new Date(System.currentTimeMillis())));
//            printer.printLineFeed();
//            printer.printLine();//----打印---


            printer.setAlignCenter();//设置居中
            printer.printLineFeed();
            printer.print(orderEntity.getUserChoiceCommpanyName());
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.printLineFeed();
            printer.print("发件人" );
            printer.printLineFeed();
            printer.print("名字 ：" + orderEntity.getCreateUserName());
            printer.printLineFeed();
            printer.print("电话：" +orderEntity.getCreateUserPhone());
            printer.printLineFeed();
            printer.print("地址：" +orderEntity.getStartStreet()+"  "+ orderEntity.getStartHouseNumber());
            printer.printLineFeed();
            printer.printLineFeed();
            printer.print("收件人");
            printer.printLineFeed();
            printer.print("名字 ：" + orderEntity.getCreateUserName());
            printer.printLineFeed();
            printer.print("电话:" +orderEntity.getReceiverPhone());
            printer.printLineFeed();
            printer.print("地址：" +orderEntity.getEndPro()+"  "+orderEntity.getEndCity()+"  " +
                    orderEntity.getEndDistrict()+"  "+orderEntity.getEndStreet()+ "　"+orderEntity.getEndHouseNumber());
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();//空白一行
//            printer.printLineFeed();
//            printer.setAlignLeft();
//            printer.print("订单状态: " + "已接单");
//            printer.printLineFeed();
//            printer.print("用户昵称: " +"周末先生");
//            printer.printLineFeed();
//            printer.print("用餐人数: " + "10人");
//            printer.printLineFeed();
//            printer.print("用餐桌号: " + "A3" + "号桌");
//            printer.printLineFeed();
//            printer.print("下单时间："+orderEntity.getCreateDate());
//            printer.printLineFeed();
//            printer.print("联系方式：" + "18094111545454");
//            printer.printLineFeed();
//            printer.printLine();
//            printer.printLineFeed();
//
//            printer.setAlignLeft();
//            printer.print("备注：" + "记得留位置");
//            printer.printLineFeed();
//            printer.printLine();
//
//            printer.printLineFeed();
//
//                printer.setAlignCenter();
//                printer.print("菜品信息");
//                printer.printLineFeed();
//                printer.setAlignCenter();
//                printer.printInOneLine("商品", "数量", "单价", 0);
//                printer.printLineFeed();
//                for (int i = 0; i < orderEntity.getGoods().size(); i++) {
//                    printer.printInOneLine(orderEntity.getGoods().get(i).getGoodsName().toString()
//                            , "X" + orderEntity.getGoods().get(i).getGoodsnum(),
//                            "￥" + orderEntity.getGoods().get(i).getGoodsPrice(), 0);
//                    printer.printLineFeed();
//                }
//                printer.printLineFeed();
//                printer.printLine();
//                printer.printLineFeed();
//                printer.setAlignLeft();
//                printer.printInOneLine("总计：", "￥" + orderEntity.getUserActualFee(), 0);

//            printer.setAlignLeft();
//            printer.printInOneLine("优惠金额：", "￥" +"0.00"
//                    , 0);
//            printer.printLineFeed();
//
//            printer.setAlignLeft();
//            printer.printInOneLine("订金/退款：", "￥" + "0.00"
//                          , 0);
//            printer.printLineFeed();
//
//
//            printer.setAlignLeft();
//            printer.printInOneLine("总计金额：", "￥" +90, 0);
//            printer.printLineFeed();

//            printer.printLine();
//            printer.printLineFeed();
//            printer.setAlignCenter();
//            printer.print("谢谢惠顾，欢迎再次光临！");
//            printer.printLineFeed();
//            printer.printLineFeed();
//            printer.printLineFeed();
            printer.feedPaperCutPartial();

            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


}
