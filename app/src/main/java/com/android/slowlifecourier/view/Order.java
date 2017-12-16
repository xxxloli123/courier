package com.android.slowlifecourier.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.android.slowlifecourier.R;
import com.android.slowlifecourier.activity.OrderDetailsActivity;
import com.android.slowlifecourier.adapter.BillAdapter;
import com.android.slowlifecourier.app.MyApplication;
import com.android.slowlifecourier.objectmodle.Info;
import com.android.slowlifecourier.objectmodle.OrderEntity;
import com.interfaceconfig.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sgrape on 2017/7/4.
 * e-mail: sgrape1153@gmail.com
 */

public class Order {
    private final View content;

    @BindView(R.id.quality)
    TextView quality;
    @BindView(R.id.address1)
    TextView address1;

    @BindView(R.id.urgent)
    TextView urgent;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_layout)
    View imgLayout;
    //    @BindView(R.id.order_num)
//    EditText orderNum;
    @BindView(R.id.urgent_layout)
    View urgentLayout;
    @BindView(R.id.insured)
    TextView insured;
    @BindView(R.id.insured_layout)
    View insuredLayout;
    @BindView(R.id.recipients_phone)
    TextView recipientsPhone;
    @BindView(R.id.recipients_name)
    EditText recipientsName;
    @BindView(R.id.commodity_type_layout)
    LinearLayout commodityNameLayout;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.commodity)
    TextView commodityTV;
    @BindView(R.id.remark_layout)
    LinearLayout remarkLayout;
    @BindView(R.id.pro)
    TextView pro;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.district)
    TextView district;
    @BindView(R.id.bill_list)
    MyListView billList;
    @BindView(R.id.commodity_ll)
    LinearLayout commodityLl;

    private OrderDetailsActivity act;
    private OrderEntity order;
    private int selectPosition = -1;
    private JSONArray areaList;
    private String imgPath;
    private int top;
    private PoiItem poiItem;
    private double endLat;
    private double endLng;
    private String urgentExpense, urgentCostFQ, endStreet;
    private double weight;

    public Order(OrderDetailsActivity act, OrderEntity orderEntity, String urgentCost, boolean isFirst) {
        this.act = act;
        content = LayoutInflater.from(act).inflate(R.layout.item_order_content, null);
        ButterKnife.bind(this, content);
        if (!isFirst){
            orderEntity.setReceiverName("");
            orderEntity.setReceiverPhone("");
            orderEntity.setEndPro("");
            orderEntity.setEndCity("");
            orderEntity.setEndDistrict("");
            orderEntity.setEndStreet("");
            orderEntity.setEndLat(0.0d);
            orderEntity.setEndLng(0.0d);
            orderEntity.setEndHouseNumber("");
            orderEntity.setUrgent("");
            orderEntity.setTradeImg(null);
        }
        init(orderEntity);
    }

    private void init(OrderEntity order) {
        if (order == null) return;
        if (order.getGoodsName()!=null)commodityTV.setText(order.getGoodsName());
        if (order.getTradeImg()!=null)Picasso.with(act).load(Config.Url.getUrl(Config.Order_Img)
                + order.getTradeImg()).into(img);
        Log.e("getTradeImg","丢了个雷姆"+order.getTradeImg());
//        ToastUtil.showToast(act,order.getTradeImg()+"");
        this.order = order;
        endLat = order.getEndLat();
        endLng = order.getEndLng();
        address1.setText(order.getEndHouseNumber());
        if (!TextUtils.equals(order.getType(), "CityWide")) {
            commodityLl.setVisibility(View.VISIBLE);
            imgLayout.setVisibility(View.VISIBLE);
//            findView(R.id.order_num_layout).setVisibility(View.VISIBLE);
            urgentLayout.setVisibility(View.GONE);
            insuredLayout.setVisibility(View.VISIBLE);
            commodityNameLayout.setVisibility(View.VISIBLE);
            remarkLayout.setVisibility(View.VISIBLE);
            quality.setText("1.0");
            findView(R.id.localtion).setVisibility(View.GONE);
            if (order.getWeight() != 1.0) {
                quality.setText("" + order.getWeight());
            }
            if (order.getEndPro() == null || order.getEndCity() == null) {
                pro.setText("");
                city.setText("");
                district.setText("");
            } else {
                pro.setText(order.getEndPro());
                city.setText(order.getEndCity());
                district.setText(order.getEndDistrict());
            }
        } else {
            address1.setText(order.getEndHouseNumber());
            pro.setText(order.getEndStreet());
            quality.setText("5.0");
            if (order.getWeight() != 5.0) {
                quality.setText("" + order.getWeight());
            }
        }
        if (order.getComment() != null) {
            remark.setText(order.getComment());
        }
        recipientsPhone.setText(order.getReceiverPhone());
        recipientsName.setText(order.getReceiverName());
        urgentExpense = String.valueOf(order.getUrgentFee());
        endStreet = order.getEndStreet();
        if (TextUtils.equals(order.getUrgent(), "yes")) {
            urgent.setTextColor(Color.RED);
            urgent.setText("是");
        } else {
            urgent.setTextColor(Color.BLACK);
            urgent.setText("否");
        }
        BillAdapter billAdapter = new BillAdapter(act, order.getGoods());
        billList.setAdapter(billAdapter);
    }

    public void update(OrderEntity orderEntity) {
        init(orderEntity);
    }

    public View getContent() {
        return content;
    }

    private <T extends View> T findView(@IdRes int id) {
        return (T) content.findViewById(id);
    }

    @OnClick({R.id.upload_front, R.id.select_area, R.id.urgent_layout, R.id.delete, R.id.localtion, R.id.commodity_type_layout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_front:
                act.cutImg(this);
                break;
            case R.id.select_area:
                if (order == null) return;
                if (!TextUtils.equals(order.getType(), "CityWide")) {
                    act.selectArea(this);
                } else {
                    act.showAreaDialog(this, selectPosition);
                }
                break;
//            case R.id.sao:
//                act.sao(this);
//                break;
            case R.id.urgent_layout:
                act.setUrgentCostFQ(this);
                if (TextUtils.equals("是", urgent.getText().toString().trim())) {
                    urgent.setTextColor(Color.BLACK);
                    urgent.setText("否");
                } else {
                    urgentExpense = urgentCostFQ;
                    Log.e("加急", "" + urgentCostFQ);
                    urgent.setTextColor(Color.RED);
                    urgent.setText("是");
                }
                break;
            case R.id.delete:
                act.deleteOrder(this);
                break;
            case R.id.localtion:
                act.localtion(this);
                break;
            case R.id.commodity_type_layout:
                selectCommodityType();
                break;
        }
    }

    public JSONObject getData() throws JSONException {
        JSONObject json = new JSONObject();
        if (isEmpty(pro.getText())) {
            Toast.makeText(act, "请选择送货区域", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!TextUtils.equals(order.getType(), "CityWide")) {
            if (isEmpty(recipientsName.getText()) || isEmpty(recipientsPhone.getText())) {
                Toast.makeText(act, "请输入收件人名字和号码", Toast.LENGTH_SHORT).show();
                return null;
            }
            if (isEmpty(commodityTV.getText())) {
                Toast.makeText(act, "请选择商品类型", Toast.LENGTH_SHORT).show();
                return null;
            }
            if (isEmpty(remark.getText())) {
                Toast.makeText(act, "请输入备注", Toast.LENGTH_SHORT).show();
            }
            if (isEmpty(address1.getText())) {
                Toast.makeText(act, "请输入详细地址", Toast.LENGTH_SHORT).show();
            }

            json.put("endPro", pro.getText());
            json.put("endCity", city.getText());
            json.put("endDistrict", district.getText());
            json.put("endStreet", "");
            json.put("endLng", "");
            json.put("endLat", "");
        } else {
//            if ((areaList == null || selectPosition == -1) && TextUtils.isEmpty(order.getEndStreet())) {
//                Toast.makeText(act, "请选择送货区域", Toast.LENGTH_SHORT).show();
//                return null;
            if (isEmpty(pro.getText())) {
                Toast.makeText(act, "请选择送货区域", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                json.put("endPro", order.getStartPro());
                json.put("endCity", order.getStartCity());
                json.put("endDistrict", order.getStartDistrict());
                json.put("endStreet", pro.getText());
                json.put("endLng", endLng);
                json.put("endLat", endLat);
            }
            if (poiItem != null) {
                json.put("endLng", poiItem.getLatLonPoint().getLongitude());
                json.put("endLat", poiItem.getLatLonPoint().getLatitude());
            }
        }
        json.put("receiverName", recipientsName.getText());
        json.put("receiverPhone", recipientsPhone.getText());

        String weight = quality.getText().toString().trim();
        if (TextUtils.isEmpty(weight)
                && !weight.matches("^\\d+\\.?\\d*$"))

        {
            Toast.makeText(act, "请输入正确的包裹重量", Toast.LENGTH_SHORT).show();
            return null;
        }

        Info info = ((MyApplication) act.getApplication()).getInfo();
        json.put("createUserId", order.getCreateUserId());
        json.put("createUserName", order.getCreateUserName());
        json.put("createUserPhone", order.getCreateUserPhone());
        json.put("type", order.getType());
        json.put("type_value", order.getType_value());
        json.put("postmanSendDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").

                format(new Date()));
        json.put("urgent", TextUtils.equals(urgent.getText().

                toString().

                trim(), "是") ? "yes" : "no");
        /**
         *
         * 加急
         */
        json.put("urgentFee", urgentExpense);
        Log.e("商品", "" + commodityTV.getText());
        json.put("goodsName", commodityTV.getText());
        json.put("insured",

                isEmpty(insured.getText().

                        toString().

                        trim()) ? "no" : "yes");
        json.put("goodsTotal",

                isEmpty(insured.getText().

                        toString().

                        trim())
                        ? order.getInsured() : insured.getText().

                        toString().

                        trim());
        json.put("goodsTotal", insured.getText().

                toString().

                trim());
        json.put("comment", remark.getText());
        json.put("weight", weight);
        json.put("postmanId", info.getId());
        json.put("postmanName", info.getName());
        json.put("postmanPhone", info.getPhone());
        json.put("postmanCommpanyId", info.getCommpany_id());
        json.put("postmanCommpanyName", info.getCommpany_name());
        json.put("userChoiceCommpanyId", order.getUserChoiceCommpanyId());
        json.put("userChoiceCommpanyName", order.getUserChoiceCommpanyName());
        json.put("userChoiceCommpanyCode", order.getUserChoiceCommpanyCode());
        json.put("startPro", order.getStartPro());
        json.put("startCity", order.getStartCity());
        json.put("startDistrict", order.getStartDistrict());
        json.put("startStreet", order.getStartStreet());
        json.put("startHouseNumber", order.getStartHouseNumber());
        json.put("startLng", order.getStartLng());
        json.put("startLat", order.getStartLat());
        json.put("receiverPhone", recipientsPhone.getText().

                toString().

                trim());
        json.put("comment", order.getComment());
        json.put("type", order.getType());

        json.put("endHouseNumber", address1.getText().

                toString().

                trim());
        json.put("orderNumber", order.getOrderNumber());
        json.put("postmanName", order.getPostmanName());
        json.put("comment", remark.getText());
        json.put("orderOfCompanyId", order.getOrderOfCompanyId());
        return json;
    }

    public void setArea(Map<String, Object> Area, JSONArray areaList, int position) {
        if (position == -1) return;
        selectPosition = position;
        if (TextUtils.equals("Intercity", order.getType())) {
            pro.setText(Area.get("pro").toString());
            city.setText(Area.get("city").toString());
            district.setText(Area.get("district").toString());
        } else {
            try {
                this.areaList = areaList;
                pro.setText(areaList.getJSONObject(position).getString("endStreet"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
        BitmapFactory.Options options = new BitmapFactory.Options();//需要一个options对象来设置图像的参数。
        options.inJustDecodeBounds = true;//这个参数为true的时候标示我们在下一步获取的old_bmp并不是一个图像，返回的只是图像的宽，高之类的数据，目的是得到图像的宽高，好自定义处理。
        Bitmap old_bmp = BitmapFactory.decodeFile(imgPath, options);//在这里我们得到图像的一些数据，path是本地图片的路径。
        options.inSampleSize = options.outWidth / img.getMeasuredWidth();//计算出缩小倍率，我现在是把宽度写死200px，你也可以获取你的ImageView的宽度，从而计算出缩小倍率。如果options.inSampleSize =  10 的话，意思是长和宽同事缩小10倍。
        options.inJustDecodeBounds = false;//这次我们需要真正的图像，所以在之前我们改为true现在要改回来。
        options.inPreferredConfig = Bitmap.Config.RGB_565;//ALPHA_8 代表8位Alpha位图ARGB_4444 代表16位ARGB位图ARGB_8888 代表32位ARGB位图RGB_565 代表8位RGB位图，感兴趣的同学可以详细的搜一下。
        options.inDither = false;    //不进行图片抖动处理
        options.inPreferredConfig = null;  //设置让解码器以最佳方式解码
        Bitmap new_bmp = BitmapFactory.decodeFile(imgPath, options);//得到我们想要的图片，也就是缩略过的。
        img.setImageBitmap(new_bmp);
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getTop() {
        return top;
    }

//    public void setOrderNum(String orderNum) {
//        this.orderNum.setText(orderNum);
//    }

    public void setAddress(PoiItem poiItem) {
//        this.address1.setText(String.format("%s%s", poiItem.getCityName(), poiItem.getSnippet()));
        this.address1.setText(poiItem.getTitle());
        this.poiItem = poiItem;
    }

    /**
     * 商品类型
     */
    public void selectCommodityType() {
        View view = LayoutInflater.from(act).inflate(R.layout.dialog_select_commodity_type, null);
        final Dialog selectCommodityTypeDialog = new AlertDialog.Builder(act, R.style.Theme_AppCompat_DayNight_Dialog).create();
        CheckBox commodity = (CheckBox) view.findViewById(R.id.commodity);
        CheckBox digital_products = (CheckBox) view.findViewById(R.id.digital_products);
        CheckBox food = (CheckBox) view.findViewById(R.id.food);
        CheckBox file = (CheckBox) view.findViewById(R.id.file);
        CheckBox clothes = (CheckBox) view.findViewById(R.id.clothes);
        CheckBox rests = (CheckBox) view.findViewById(R.id.rests);
        selectCommodityTypeDialog.show();
        selectCommodityTypeDialog.setContentView(view);
        View delete = view.findViewById(R.id.back_rl);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCommodityTypeDialog.dismiss();
            }
        });
        commodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("日用品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        digital_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("数码产品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("食品");
                selectCommodityTypeDialog.dismiss();
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("文件");
                selectCommodityTypeDialog.dismiss();
            }
        });
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("衣物");
                selectCommodityTypeDialog.dismiss();
            }
        });
        rests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodityTV.setText("其他");
                selectCommodityTypeDialog.dismiss();
            }
        });
    }

    public void setUrgentCost(String s) {
        urgentCostFQ = s;
    }

    @OnClick(R.id.commodity_ll)
    public void onViewClicked() {
        if (billList.getVisibility() == View.GONE) billList.setVisibility(View.VISIBLE);
        else billList.setVisibility(View.GONE);
    }

}
