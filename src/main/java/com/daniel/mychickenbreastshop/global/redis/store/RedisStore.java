package com.daniel.mychickenbreastshop.global.redis.store;

/**
 * Redis Component 클래스
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링
 *         1.2 2022.10.11 확장을 위해 인터페이스로 변경
 *     </History>
 *
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */

public interface RedisStore {

    /* key를 통해 value 리턴 */
    <T> T getData(String key, Class<T> type);

    /* 데이터 삽입 */
    void setData(String key, Object value);

    /* 유효 시간 동안 (key, value) 저장 */
    void setDataExpire(String key, Object value, long duration);

    /* 데이터 삭제 */
    Boolean deleteData(String key);

}
