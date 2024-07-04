package org.duckdns.omaju.core.exeption;

public class RejoinNotAllowedException extends Exception {
    public RejoinNotAllowedException() {
        super("회원가입이 아직 불가한 상태입니다. 회원탈퇴 시점으로 부터 1시간이 지난 후 다시 시도해주세요.");
    }
}