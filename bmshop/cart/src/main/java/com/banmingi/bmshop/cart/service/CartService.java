package com.banmingi.bmshop.cart.service;

import com.banmingi.bmshop.auth.pojo.UserInfo;
import com.banmingi.bmshop.cart.client.GoodsClient;
import com.banmingi.bmshop.cart.interceptor.LoginInterceptor;
import com.banmingi.bmshop.cart.pojo.Cart;
import com.banmingi.bmshop.common.utils.JsonUtils;
import com.banmingi.bmshop.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther 半命i 2019/12/2
 * @description
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    /**
     * 添加商到购物车.
     * @param cart
     */
    public void addCart(Cart cart) {

        //获取用户信息.
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //查询购物车记录.
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        //判断当前商品是否在购物车中
        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        if (hashOperations.hasKey(key)) {
            //在,更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson,Cart.class);
            cart.setNum(cart.getNum() + num);

        } else {
            //不在,新增购物车
            Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOperations.put(key,JsonUtils.serialize(cart));

    }

    /**
     * 查询购物车.
     * @return
     */
    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return null;
        }

        //获取用户购物车记录
        BoundHashOperations<String, Object, Object> hashOperations =
                this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        List<Object> cartsJson = hashOperations.values();

        // 购物车集合为空
        if (CollectionUtils.isEmpty(cartsJson)) {
            return null;
        }

        //把List<Object> 转化为List<Cart>
        return cartsJson.stream().map( cartJson -> JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());
    }


    /**
     * 更新购物车数量.
     * @param cart
     */
    public void updateNum(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return;
        }

        //获取用户购物车记录
        BoundHashOperations<String, Object, Object> hashOperations =
                this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        Integer num = cart.getNum();
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson,Cart.class);
        cart.setNum(cart.getNum() + num);

        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }
}
