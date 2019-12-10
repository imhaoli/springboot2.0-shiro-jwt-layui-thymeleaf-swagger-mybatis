package com.xh.lesson.service;

import com.xh.lesson.exception.BusinessException;
import com.xh.lesson.exception.code.BaseResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisService
 * TODO:类文件简单描述
 * @Author: 小霍
 * @CreateDate: 2019/9/6 13:47
 * @UpdateUser: 小霍
 * @UpdateDate: 2019/9/6 13:47
 * @Version: 0.0.1
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /** -------------------key相关操作--------------------- */

    /**
     * 删除key
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:25
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:25
     * @Version:     0.0.1
     * @param key
     * @return       Boolean  成功返回true 失败返回false
     * @throws
     */
    public Boolean delete(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:27
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:27
     * @Version:     0.0.1
     * @param keys
     * @return       Long 返回成功删除key的数量
     * @throws
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }



    /**
     * 是否存在key
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:30
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:30
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean hasKey(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:35
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:35
     * @Version:     0.0.1
     * @param key
     * @param timeout
     * @param unit
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        if (null==key||null==unit){
            return false;
        }
        return redisTemplate.expire(key, timeout, unit);
    }



    /**
     * 查找匹配的key
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:38
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:38
     * @Version:     0.0.1
     * @param pattern
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<String> keys(String pattern) {
        if (null==pattern){
            return null;
        }
        return redisTemplate.keys(pattern);
    }



    /**
     * 移除 key 的过期时间，key 将持久保持
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:41
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:41
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean persist(String key) {
        if (null==key){
            return false;
        }
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:43
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:43
     * @Version:     0.0.1
     * @param key
     * @param unit
     * @return       java.lang.Long 当 key 不存在时，返回 -2 。当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以秒为单位，返回 key的剩余生存时间
     * @throws
     */
    public Long getExpire(String key, TimeUnit unit) {
        if(null==key||null==unit){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key or TomeUnit 不能为空");
        }
        return redisTemplate.getExpire(key, unit);
    }

    //*************String相关数据类型***************************
    /**
     * 设置指定 key 的值
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:50
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:50
     * @Version:     0.0.1
     * @param key
     * @param value
     * @return       void
     * @throws
     */
    public void set(String key, Object value) {

        if(null==key||null==value){
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }
    /**
     * 设置key 的值 并设置过期时间
     * @Author:      小霍
     * @CreateDate:  2019/8/27 20:55
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 20:55
     * @Version:     0.0.1
     * @param key
     * @param value
     * @param time
     * @param unit
     * @return       void
     * @throws
     */
    public void set(String key,Object value,long time,TimeUnit unit){

        if(null==key||null==value||null==unit){
            return;
        }
        if(time<0){
            redisTemplate.opsForValue().set(key,value);
        }else {
            redisTemplate.opsForValue().set(key,value,time,unit);
        }

    }
    /**
     * 设置key 的值 并设置过期时间
     * key存在 不做操作返回false
     * key不存在设置值返回true
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:05
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:05
     * @Version:     0.0.1
     * @param key
     * @param value
     * @param time
     * @param unit
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean setifAbsen(String key,Object value,long time,TimeUnit unit){

        if(null==key||null==value||null==unit){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"kkey、value、unit都不能为空");
        }
        return redisTemplate.opsForValue().setIfAbsent(key,value,time,unit);
    }
    /**
     * 获取指定Key的Value。如果与该Key关联的Value不是string类型，Redis将抛出异常，
     * 因为GET命令只能用于获取string Value，如果该Key不存在，返回null
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:10
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:10
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Object
     * @throws
     */
    public Object get(String key){

        if(null==key){
            return null;
        }
        return  redisTemplate.opsForValue().get(key);
    }
    /**
     * 很明显先get再set就说先获取key值对应的value然后再set 新的value 值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:15
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:15
     * @Version:     0.0.1
     * @param key
     * @param value
     * @return       java.lang.Object
     * @throws
     */
    public Object getSet(String key,Object value){

        if(null==key){
            return null;
        }
        return redisTemplate.opsForValue().getAndSet(key,value);
    }
    /**
     * 通过批量的key获取批量的value
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:20
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:20
     * @Version:     0.0.1
     * @param keys
     * @return       java.util.List<java.lang.Object>
     * @throws
     */
    public List<Object> mget(Collection<String> keys){

        if(null==keys){
            return Collections.emptyList();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }
    /**
     *  将指定Key的Value原子性的增加increment。如果该Key不存在，其初始值为0，在incrby之后其值为increment。
     *  如果Value的值不能转换为整型值，如Hi，该操作将执行失败并抛出相应异常。操作成功则返回增加后的value值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:25
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:25
     * @Version:     0.0.1
     * @param key
     * @param increment
     * @return       long
     * @throws
     */
    public long incrby(String key,long increment){
        if(null==key){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key不能为空");
        }
        return redisTemplate.opsForValue().increment(key,increment);
    }
    /**
     *
     * 将指定Key的Value原子性的减少decrement。如果该Key不存在，其初始值为0，
     * 在decrby之后其值为-decrement。如果Value的值不能转换为整型值，
     * 如Hi，该操作将执行失败并抛出相应的异常。操作成功则返回减少后的value值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:30
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:30
     * @Version:     0.0.1
     * @param key
     * @param decrement
     * @return       java.lang.Long
     * @throws
     */
    public Long decrby(String key,long decrement){
        if(null==key){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key不能为空");
        }
        return redisTemplate.opsForValue().decrement(key,decrement);
    }
    /**
     *  如果该Key已经存在，APPEND命令将参数Value的数据追加到已存在Value的末尾。如果该Key不存在，
     *  APPEND命令将会创建一个新的Key/Value。返回追加后Value的字符串长度。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 21:35
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 21:35
     * @Version:     0.0.1
     * @param key
     * @param value
     * @return       java.lang.Integer
     * @throws
     */
    public Integer append(String key,String value){
        if(key==null){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key不能为空");
        }
        return redisTemplate.opsForValue().append(key,value);
    }
//******************hash数据类型*********************
    /**
     * 通过key 和 field 获取指定的 value
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:35
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:35
     * @Version:     0.0.1
     * @param key
     * @param field
     * @return       java.lang.Object
     * @throws
     */
    public Object hget(String key, Object field) {
        if(null==key||null==field){
            return null;
        }
        return redisTemplate.opsForHash().get(key,field);
    }

    /**
     * 为指定的Key设定Field/Value对，如果Key不存在，该命令将创建新Key以用于存储参数中的Field/Value对，
     * 如果参数中的Field在该Key中已经存在，则用新值覆盖其原有值。
     * 返回1表示新的Field被设置了新值，0表示Field已经存在，用新值覆盖原有值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:39
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:39
     * @Version:     0.0.1
     * @param key
     * @param field
     * @param value
     * @return
     * @throws
     */
    public void hset(String key, Object field, Object value) {
        if(null==key||null==field){
            return;
        }
        redisTemplate.opsForHash().put(key,field,value);
    }

    /**
     * 判断指定Key中的指定Field是否存在，返回true表示存在，false表示参数中的Field或Key不存在。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:41
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:41
     * @Version:     0.0.1
     * @param key
     * @param field
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean hexists(String key, Object field) {
        if(null==key||null==field){
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key,field);
    }

    /**
     * 从指定Key的Hashes Value中删除参数中指定的多个字段，如果不存在的字段将被忽略，
     * 返回实际删除的Field数量。如果Key不存在，则将其视为空Hashes，并返回0。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:45
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:45
     * @Version:     0.0.1
     * @param key
     * @param fields
     * @return       java.lang.Long
     * @throws
     */
    public Long hdel(String key, Object... fields) {
        if(null==key||null==fields||fields.length==0){
            return 0L;
        }
        return redisTemplate.opsForHash().delete(key,fields);
    }


    /**
     * 通过key获取所有的field和value
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:50
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:50
     * @Version:     0.0.1
     * @param key
     * @return       java.util.Map<java.lang.Object,java.lang.Object>
     * @throws
     */
    public Map<Object, Object> hgetall(String key) {
        if(key==null){
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 逐对依次设置参数中给出的Field/Value对。如果其中某个Field已经存在，则用新值覆盖原有值。
     * 如果Key不存在，则创建新Key，同时设定参数中的Field/Value。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:55
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:55
     * @Version:     0.0.1
     * @param key
     * @param hash
     * @return
     * @throws
     */
    public void hmset(String key, Map<String, Object> hash) {

        if(null==key||null==hash){
            return;
        }
        redisTemplate.opsForHash().putAll(key,hash);
    }

    /**
     * 获取和参数中指定Fields关联的一组Values，其返回顺序等同于Fields的请求顺序。
     * 如果请求的Field不存在，其值对应的value为null。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 22:59
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 22:59
     * @Version:     0.0.1
     * @param key
     * @param fields
     * @return       java.util.List<java.lang.Object>
     * @throws
     */
    public List<Object> hmget(String key, Collection<Object> fields) {

        if(null==key||null==fields){
            return null;
        }

        return redisTemplate.opsForHash().multiGet(key,fields);
    }

    /**
     * 对应key的字段自增相应的值
     * @Author:      小霍
     * @CreateDate:   2019/8/27 23:03
     * @UpdateUser:
     * @UpdateDate:   2019/8/27 23:03
     * @Version:     0.0.1
     * @param key
     * @param field
     * @param increment
     * @return       java.lang.Long
     * @throws
     */
    public Long hIncrBy(String key,Object field,long increment){
        if (null==key||null==field){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key or field 不能为空");
        }
        return redisTemplate.opsForHash().increment(key,field,increment);

    }
    //***************List数据类型***************
    /**
     * 向列表左边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的头部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:23
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:23
     * @Version:     0.0.1
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return       java.lang.Long 返回操作的value个数
     * @throws
     */
    public Long lpush(String key, Object... strs) {
        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key,strs);
    }

    /**
     * 向列表右边添加元素。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，之后再将数据从链表的尾部插入。
     * 如果该键的Value不是链表类型，该命令将将会抛出相关异常。操作成功则返回插入后链表中元素的数量。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:26
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:26
     * @Version:     0.0.1
     * @param key
     * @param strs 可以使一个string 也可以使string数组
     * @return       java.lang.Long 返回操作的value个数
     * @throws
     */
    public Long rpush(String key, Object... strs) {
        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().rightPushAll(key,strs);
    }
    /**
     * 返回并弹出指定Key关联的链表中的第一个元素，即头部元素。如果该Key不存在，
     * 返回nil。LPOP命令执行两步操作：第一步是将列表左边的元素从列表中移除，第二步是返回被移除的元素值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:29
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:29
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Object
     * @throws
     */
    public Object lpop(String key) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 返回并弹出指定Key关联的链表中的最后一个元素，即头部元素。如果该Key不存在，返回nil。
     * RPOP命令执行两步操作：第一步是将列表右边的元素从列表中移除，第二步是返回被移除的元素值。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:33
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:33
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Object
     * @throws
     */
    public Object rpop(String key) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     *该命令的参数start和end都是0-based。即0表示链表头部(leftmost)的第一个元素。
     * 其中start的值也可以为负值，-1将表示链表中的最后一个元素，即尾部元素，-2表示倒数第二个并以此类推。
     * 该命令在获取元素时，start和end位置上的元素也会被取出。如果start的值大于链表中元素的数量，
     * 空链表将会被返回。如果end的值大于元素的数量，该命令则获取从start(包括start)开始，链表中剩余的所有元素。
     * 注：Redis的列表起始索引为0。显然，LRANGE numbers 0 -1 可以获取列表中的所有元素。返回指定范围内元素的列表。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:38
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:38
     * @Version:     0.0.1
     * @param key
     * @param start
     * @param end
     * @return       java.util.List<java.lang.Object>
     * @throws
     */
    public List<Object> lrange(String key, long start, long end) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:43
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:43
     * @Version:     0.0.1
     * @param key
     * @param start
     * @param end
     * @return
     * @throws
     */
    public void ltrim(String key, long start, long end) {
        if(null==key){
            return;
        }
        redisTemplate.opsForList().trim(key,start,end);
    }

    /**
     * 该命令将返回链表中指定位置(index)的元素，index是0-based，表示从头部位置开始第index的元素，
     * 如果index为-1，表示尾部元素。如果与该Key关联的不是链表，该命令将返回相关的错误信息。 如果超出index返回这返回nil。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:48
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:48
     * @Version:     0.0.1
     * @param key
     * @param index
     * @return       java.lang.Object
     * @throws
     */
    public Object lindex(String key, long index) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForList().index(key,index);
    }

    /**
     * 返回指定Key关联的链表中元素的数量，如果该Key不存在，则返回0。如果与该Key关联的Value的类型不是链表，则抛出相关的异常。
     * @Author:      小霍
     * @CreateDate:  2019/8/27 23:51
     * @UpdateUser:
     * @UpdateDate:  2019/8/27 23:51
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long
     * @throws
     */
    public Long llen(String key) {

        if(null==key){
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }
    //***************Set数据类型*************
    /**
     * 如果在插入的过程用，参数中有的成员在Set中已经存在，该成员将被忽略，而其它成员仍将会被正常插入。
     * 如果执行该命令之前，该Key并不存在，该命令将会创建一个新的Set，此后再将参数中的成员陆续插入。返回实际插入的成员数量。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:20
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:20
     * @Version:     0.0.1
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return       java.lang.Long 添加成功的个数
     * @throws
     */
    public Long sadd(String key, Object... members) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().add(key, members);

    }

    /**
     * 返回Set中成员的数量，如果该Key并不存在，返回0。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:25
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:25
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long
     * @throws
     */
    public Long scard(String key) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().size(key);

    }

    /**
     * 判断参数中指定成员是否已经存在于与Key相关联的Set集合中。返回true表示已经存在，false表示不存在，或该Key本身并不存在。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:29
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:29
     * @Version:     0.0.1
     * @param key
     * @param member
     * @return       java.lang.Boolean
     * @throws
     */
    public Boolean sismember(String key, Object member) {
        if (null==key){
            return false;
        }
        return redisTemplate.opsForSet().isMember(key,member);

    }

    /**
     * 和SPOP一样，随机的返回Set中的一个成员，不同的是该命令并不会删除返回的成员。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:31
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:31
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.String
     * @throws
     */
    public Object srandmember(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().randomMember(key);

    }
    /**
     * 和SPOP一样，随机的返回Set中的一个成员，不同的是该命令并不会删除返回的成员。
     * 还可以传递count参数来一次随机获得多个元素，根据count的正负不同，具体表现也不同。
     * 当count 为正数时，SRANDMEMBER 会随机从集合里获得count个不重复的元素。
     * 如果count的值大于集合中的元素个数，则SRANDMEMBER 会返回集合中的全部元素。
     * 当count为负数时，SRANDMEMBER 会随机从集合里获得|count|个的元素，如果|count|大与集合中的元素，
     * 就会返回全部元素不够的以重复元素补齐，如果key不存在则返回nil。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:39
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:39
     * @Version:     0.0.1
     * @param key
     * @param count
     * @return       java.util.List<java.lang.String>
     * @throws
     */
    public List<Object> srandmember(String key,int count) {
        if(null==key){
            return null;
        }
        return redisTemplate.opsForSet().randomMembers(key,count);

    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:47
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:47
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.String
     * @throws
     */
    public Object spop(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().pop(key);

    }

    /**
     * 通过key获取set中所有的value
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:51
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:51
     * @Version:     0.0.1
     * @param key
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<Object> smembers(String key) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForSet().members(key);

    }
    /**
     * 从与Key关联的Set中删除参数中指定的成员，不存在的参数成员将被忽略，
     * 如果该Key并不存在，将视为空Set处理。返回从Set中实际移除的成员数量，如果没有则返回0。
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:55
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:55
     * @Version:     0.0.1
     * @param key
     * @param members
     * @return       java.lang.Long
     * @throws
     */
    public Long srem(String key, Object... members) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForSet().remove(key,members);

    }

    /**
     * 将元素value从一个集合移到另一个集合
     * @Author:      小霍
     * @CreateDate:  2019/8/28 21:02
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 21:02
     * @Version:     0.0.1
     * @param srckey
     * @param dstkey
     * @param member
     * @return       java.lang.Long
     * @throws
     */
    public Boolean smove(String srckey, String dstkey, Object member) {
        if (null==srckey||null==dstkey){
            return false;
        }
        return redisTemplate.opsForSet().move(srckey,member,dstkey);

    }


    /**
     * 获取两个集合的并集
     * @Author:      小霍
     * @CreateDate:  2019/8/28 20:08
     * @UpdateUser:
     * @UpdateDate:  2019/8/28 20:08
     * @Version:     0.0.1
     * @param key
     * @param otherKeys
     * @return       java.util.Set<java.lang.Object> 返回两个集合合并值
     * @throws
     */
    public Set<Object> sUnion(String key, String otherKeys) {
        if (null==key||otherKeys==null){
            return null;
        }
        return redisTemplate.opsForSet().union(key, otherKeys);
    }
    //**********Sorted Set 数据类型********************
    /**
     *添加参数中指定的所有成员及其分数到指定key的Sorted Set中，在该命令中我们可以指定多组score/member作为参数。
     * 如果在添加时参数中的某一成员已经存在，该命令将更新此成员的分数为新值，同时再将该成员基于新值重新排序。
     * 如果键不存在，该命令将为该键创建一个新的Sorted Set Value，并将score/member对插入其中。
     * @Author:      小霍
     * @CreateDate:  2019/8/13 23:28
     * @UpdateUser:
     * @UpdateDate:  2019/8/13 23:28
     * @Version:     0.0.1
     * @param key
     * @param score
     * @param member
     * @return       java.lang.Long
     * @throws
     */
    public Boolean zadd(String key, double score, Object member) {
        if (null==key){
            return false;
        }
        return redisTemplate.opsForZSet().add(key,member,score);

    }


    /**
     * 该命令将移除参数中指定的成员，其中不存在的成员将被忽略。
     * 如果与该Key关联的Value不是Sorted Set，相应的错误信息将被返回。 如果操作成功则返回实际被删除的成员数量。
     * @Author:      小霍
     * @CreateDate:  2019/8/13 23:26
     * @UpdateUser:
     * @UpdateDate:  2019/8/13 23:26
     * @Version:     0.0.1
     * @param key
     * @param members 可以使一个string 也可以是一个string数组
     * @return       java.lang.Long
     * @throws
     */
    public Long zrem(String key, Object... members) {
        if(null==key||null==members){
            return 0L;
        }
        return redisTemplate.opsForZSet().remove(key,members);

    }

    /**
     * 返回Sorted Set中的成员数量，如果该Key不存在，返回0。
     * @Author:      小霍
     * @CreateDate:  2019/8/13 23:25
     * @UpdateUser:
     * @UpdateDate:  2019/8/13 23:25
     * @Version:     0.0.1
     * @param key
     * @return       java.lang.Long
     * @throws
     */
    public Long zcard(String key) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 该命令将为指定Key中的指定成员增加指定的分数。如果成员不存在，该命令将添加该成员并假设其初始分数为0，
     * 此后再将其分数加上increment。如果Key不存在，该命令将创建该Key及其关联的Sorted Set，
     * 并包含参数指定的成员，其分数为increment参数。如果与该Key关联的不是Sorted Set类型，
     * 相关的错误信息将被返回。如果不报错则以串形式表示的新分数。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 11:54
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 11:54
     * @Version:     0.0.1
     * @param key
     * @param score
     * @param member
     * @return       java.lang.Double
     * @throws
     */
    public Double zincrby(String key, double score, Object member) {
        if (null==key){
            throw new BusinessException(BaseResponseCode.DATA_ERROR.getCode(),"key 不能为空");
        }
        return redisTemplate.opsForZSet().incrementScore(key,member,score);
    }

    /**
     * 该命令用于获取分数(score)在min和max之间的成员数量。
     * （min=<score<=max）如果加上了“(”着表明是开区间例如zcount key (min max 则 表示（min<score=<max）
     * 同理zcount key min (max 则表明(min=<score<max) 返回指定返回数量。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 11:55
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 11:55
     * @Version:     0.0.1
     * @param key
     * @param min
     * @param max
     * @return       java.lang.Long
     * @throws
     */
    public Long zcount(String key, double min, double max) {
        if (null==key){
            return 0L;
        }
        return redisTemplate.opsForZSet().count(key, min, max);

    }

    /**
     * Sorted Set中的成员都是按照分数从低到高的顺序存储，该命令将返回参数中指定成员的位置值，
     * 其中0表示第一个成员，它是Sorted Set中分数最低的成员。 如果该成员存在，则返回它的位置索引值。否则返回nil。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 12:00
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 12:00
     * @Version:     0.0.1
     * @param key
     * @param member
     * @return       java.lang.Long
     * @throws
     */
    public Long zrank(String key, Object member) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().rank(key,member);

    }

    /**
     * 如果该成员存在，以字符串的形式返回其分数，否则返回null
     * @Author:      小霍
     * @CreateDate:  2019/8/18 11:58
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 11:58
     * @Version:     0.0.1
     * @param key
     * @param member
     * @return       java.lang.Double
     * @throws
     */
    public Double zscore(String key, Object member) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().score(key,member);
    }

    /**
     * 该命令返回顺序在参数start和stop指定范围内的成员，这里start和stop参数都是0-based，即0表示第一个成员，-1表示最后一个成员。如果start大于该Sorted
     * Set中的最大索引值，或start > stop，此时一个空集合将被返回。如果stop大于最大索引值，
     * 该命令将返回从start到集合的最后一个成员。如果命令中带有可选参数WITHSCORES选项，
     * 该命令在返回的结果中将包含每个成员的分数值，如value1,score1,value2,score2...。
     * @Author:      小霍
     * @CreateDate:  2019/8/13 23:27
     * @UpdateUser:
     * @UpdateDate:  2019/8/13 23:27
     * @Version:     0.0.1
     * @param key
     * @param min
     * @param max
     * @return       java.util.Set<java.lang.String> 指定区间内的有序集成员的列表。
     * @throws
     */
    public Set<Object> zrange(String key, long min, long max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().range(key, min, max);

    }
    /**
     * 该命令的功能和ZRANGE基本相同，唯一的差别在于该命令是通过反向排序获取指定位置的成员，
     * 即从高到低的顺序。如果成员具有相同的分数，则按降序字典顺序排序。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 12:06
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 12:06
     * @Version:     0.0.1
     * @param key
     * @param start
     * @param end
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().reverseRange(key, start, end);

    }

    /**
     * 该命令将返回分数在min和max之间的所有成员，即满足表达式min <= score <= max的成员，
     * 其中返回的成员是按照其分数从低到高的顺序返回，如果成员具有相同的分数，
     * 则按成员的字典顺序返回。可选参数LIMIT用于限制返回成员的数量范围。
     * 可选参数offset表示从符合条件的第offset个成员开始返回，同时返回count个成员。
     * 可选参数WITHSCORES的含义参照ZRANGE中该选项的说明。*最后需要说明的是参数中min和max的规则可参照命令ZCOUNT。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 12:08
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 12:08
     * @Version:     0.0.1
     * @param key
     * @param max
     * @param min
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<Object> zrangebyscore(String key, double min, double max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);

    }



    /**
     * 该命令除了排序方式是基于从高到低的分数排序之外，其它功能和参数含义均与ZRANGEBYSCORE相同。
     * 需要注意的是该命令中的min和max参数的顺序和ZRANGEBYSCORE命令是相反的。
     * @Author:      小霍
     * @CreateDate:  2019/8/18 12:13
     * @UpdateUser:
     * @UpdateDate:  2019/8/18 12:13
     * @Version:     0.0.1
     * @param key
     * @param max
     * @param min
     * @return       java.util.Set<java.lang.String>
     * @throws
     */
    public Set<Object> zrevrangeByScore(String key, double min, double max) {
        if (null==key){
            return null;
        }
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }
}
