package la.service.common.entity;

/**
 * Created by wangjia on 14-6-26.
 */
public class MusicRspMessage extends RspMessage {
    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
