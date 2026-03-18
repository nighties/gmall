package com.gmall.common.util;

/**
 * 雪花算法 ID 生成器
 */
public class SnowflakeIdGenerator {

    /**
     * 起始时间戳 (2026-01-01 00:00:00)
     */
    private static final long START_TIMESTAMP = 1735689600000L;

    /**
     * 机器 ID 位数
     */
    private static final long MACHINE_BITS = 5L;

    /**
     * 数据中心 ID 位数
     */
    private static final long DATA_CENTER_BITS = 5L;

    /**
     * 序列号位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器 ID 最大值
     */
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BITS);

    /**
     * 数据中心 ID 最大值
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_BITS);

    /**
     * 序列号最大值
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器 ID 位移位数
     */
    private static final long MACHINE_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心 ID 位移位数
     */
    private static final long DATA_CENTER_SHIFT = SEQUENCE_BITS + MACHINE_BITS;

    /**
     * 时间戳位移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_BITS + DATA_CENTER_BITS;

    /**
     * 数据中心 ID
     */
    private final long dataCenterId;

    /**
     * 机器 ID
     */
    private final long machineId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param dataCenterId 数据中心 ID
     * @param machineId    机器 ID
     */
    public SnowflakeIdGenerator(long dataCenterId, long machineId) {
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("数据中心 ID 必须在 0~%d 之间", MAX_DATA_CENTER_ID));
        }
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(
                    String.format("机器 ID 必须在 0~%d 之间", MAX_MACHINE_ID));
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 生成下一个 ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 时钟回拨检查
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，拒绝生成 ID");
        }

        // 同一毫秒内，序列号自增
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 序列号溢出，等待下一毫秒
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 组合生成 ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_SHIFT)
                | (machineId << MACHINE_SHIFT)
                | sequence;
    }

    /**
     * 等待下一毫秒
     *
     * @param lastTimestamp 上次时间戳
     * @return 下一毫秒时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 获取单例实例 (数据中心 ID=0, 机器 ID=0)
     *
     * @return 单例实例
     */
    private static class SingletonHolder {
        private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator(0, 0);
    }

    public static SnowflakeIdGenerator getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 生成 ID (使用默认实例)
     *
     * @return ID
     */
    public static long generateId() {
        return getInstance().nextId();
    }
}
