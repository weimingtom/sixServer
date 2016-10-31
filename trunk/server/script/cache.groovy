import org.rojo.repository.Rojo;
import org.rojo.util.*;

  long write=Rojo.write();
   long read=Rojo.read();
   Stats st=Rojo.getCache().stats();
   int size=st.getSize();
   long mis=st.getMisses();
   long hit=st.getHits();
   long ev=st.getEvictions();
   long look=st.getLookups();
   long put=st.getPuts();

println("当前缓存数量："+size);//计算cache数量
println("命中："+hit);//计算cache数量
println("丢失："+mis);//计算cache数量
rate=hit+mis==0?0:hit/(hit+mis);
println("命中率："+rate);
println("写入次数："+write);
println("读取次数："+read);
println("逃离缓存："+ev);
println("写入缓存："+put);