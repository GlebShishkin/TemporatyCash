package ru.stepup.geometry;

import java.util.concurrent.ConcurrentHashMap;

public class ClearCacheThread implements Runnable {
    private volatile ConcurrentHashMap<Long, DataLive> map;
    private long    timeout;
    private boolean isActive;
    ClearCacheThread(ConcurrentHashMap map, long timeout)
    {
        this.map = map;
        this.timeout = timeout;
        this.isActive = true;
    }
    @Override
    public void run()
    {
        synchronized(map) {
            while(isActive){
                try {
                    Thread.sleep(this.timeout); //  величина задержки берется из @Cache(1000)
                    long current = System.currentTimeMillis(); // лпределяем тек. время, чтобы понять: протух кэш по времени или нет
                    for (long k : this.map.keySet()) {
                        // isLive удаляем только тех у кого не обновлялся параметр DataLiveю.lastTime (последнее обращение)
                        if (this.map.get(k).isLive(current, this.timeout))
                            this.map.remove(k);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void disable(){
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
}
