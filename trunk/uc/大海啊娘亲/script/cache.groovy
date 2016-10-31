import org.rojo.repository.Rojo;

cur=Rojo.getCachedObjectCounter();
hit=Rojo.hit();
miss=Rojo.miss();
println("当前缓存数量："+cur);//计算cache数量
println("命中："+hit);//计算cache数量
println("丢失："+miss);//计算cache数量
rate=hit+miss==0?0:hit/(hit+miss);
println("命中率："+rate);
println("写入次数："+Rojo.write());
println("读取次数："+Rojo.read());