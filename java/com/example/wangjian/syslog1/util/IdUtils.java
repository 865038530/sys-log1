package com.example.wangjian.syslog1.util;

import org.apache.ibatis.logging.LogException;
import org.springframework.util.IdGenerator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

public class IdUtils {
    /**
     * 获取本机ip
     *
     * @return
     */
    private static String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":") == -1) {
                        System.out.println("本机的IP = " + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String createMachineNumberUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV).replace("0", "");
    }

//    public static Long getId() {
//        return IdGenerator.getId();
//    }

    public static Long getSerialNo() {
        Long machineNumberUUId = Long.valueOf(createMachineNumberUUId());
        Long machineId = machineNumberUUId % 32;
        SnowFlake snowFlake = new SnowFlake(0L, machineId);
        return snowFlake.nextId();
    }

    /**
     * 生成quantity个digits位数随机码
     *
     * @param quantity 多少个
     * @param digits   多少位数
     */
    public static String[] randomCode(int quantity, int digits) {
        if (digits <= 0 || quantity <= 0) {
//			throw new LogException("quantity or digits cannot be less than or equal to 0");
        }
        int bound = 10;
        for (int i = 1; i < digits; i++) {
            bound = bound * 10;
        }
        if (quantity > bound) {
//			throw new LogException("random over limit");
        }
        String format = "%0digitsd".replace("digits", digits + "");
        Random myRandom = new Random();
        String[] result = new String[quantity];
        int[] luckNums = new int[quantity];
        //生成quantity个不同的随机数
        for (int i = 0; i < quantity; i++) {
            int randomNum = myRandom.nextInt(bound);
            while (existed(randomNum, luckNums, i)) {
                randomNum = myRandom.nextInt(bound);
            }
            luckNums[i] = randomNum;
            String randomCode = String.format(format, randomNum);
            result[i] = randomCode;
        }
        return result;
    }

    /**
     * 检验新生成的随机数是否已经存在
     *
     * @param num      新产生的随机数
     * @param luckNums 当前随机数数组
     * @param index    当前随机数的索引
     * @return boolean 是否存在，存在则返回true，不存在则返回false
     */
    public static boolean existed(int num, int[] luckNums, int index) {
        boolean contains = (num + "").contains("4");//去除带4随机码
        if (contains) {
            return true;
        }
        for (int i = 0; i < index; i++) {
            if (num == luckNums[i]) {
                return true;
            }
            //这个地方还可以增加一些处理，用来筛选不想要的随机码
        }
        return false;
    }

    public static void main(String[] args) {
        getQuantityOrderSerialNo(20);
    }

    //生成20位订单编号  snowFlake算法+2位随机码
    public static synchronized String getOrderSerialNo() {
        String orderSerialNo = getSerialNo() + randomCode(1, 2)[0];
        return orderSerialNo;
    }

    //生成quantity个20位订单编号
    public static synchronized String[] getQuantityOrderSerialNo(int quantity) {
        String[] orderSerialNos = new String[quantity];
        String[] randomCode = randomCode(quantity, 2);
        for (int i = 0; i < randomCode.length; i++) {
            orderSerialNos[i] = getSerialNo() + randomCode[i];
            System.out.println(getSerialNo() + randomCode[i]);
        }
        return orderSerialNos;
    }
}

