import static sys.*
import static drunkard.*

//name="straw"
name="flaxfibre"
//name="flaxseed"

INV = 56
RNG = 50


void collect() {
def i=0
while(findObjectByName("",RNG)) 
{
doClick(findObjectByName(name,RNG),3,0);
Thread.sleep 200
i++
//if(i>=INV)
//return
}
}
collect()