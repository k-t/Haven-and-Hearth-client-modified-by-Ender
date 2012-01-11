import static sys.*
import static drunkard.*
import util.AreaWalker


/************************************************
*****  Start in Northwest Corner            *****
************************************************/

/******************************
** user modifiable variables **
******************************/

flower_stage = 3; 		//Only need to change for Hemp, leave at 3 for others.
rePlant = 0; 			// Replant seeds? 1 - Yes , 0 - No
inventory_Size = 30; 	//Inventory size
use_Cupboards = 0; 		// Use Cupboards? 1 - Yes , 0 - No
rouse = 0; 				// 1 - Yes, 0 - No
water = 0; 				// 1 - Yes, 0 - No
min_Space = 5;			//Minimum Space left in inventory
pumpkin=0;			//1- yes 0- no needs empty inventory slices&drops pumpkin to ground
/******************************
** Do not modify below       **
******************************/
masInd = 0;
current_X = 0;
current_Y = 0;
items = -1;
inventory_Space = 90;
buckets = 0;
cb_Items = 0;
waterskins = 0;

main();
/******************************
**     Delicious content     **
******************************/

void main() {
    def a = selectArea("Select area to harvest.")
    if (a == null)
        return;
    def walker = new AreaWalker(a)
    for (tile in walker.eachTile()) {
        tile_work(tile)
    }
}

void tile_work(tile)
 {
    if( Stamina < 65 )
     {	
     print "refill sta" 
        refill_stam();
		print "refill sta end"
     }
//TODO	 	 
//    check_Inventory();
    processHarvestTile(tile);
    if (rePlant == 1)
     {
        plantSeed ();
     }
   if(pumpkin) pumpkinate();
 }
 
/********************************************************
*****                 HARVESTING/PLANTING           *****
********************************************************/

 void processHarvestTile(tile) 
  { 
     int id; 
     int stage;
     def p = getMyCoord()
     id = findMapObject("", 2, tile.tileX - p.tileX, tile.tileY - p.tileY); 
     if (id > 0) { 
         stage = getObjectBlob(id, 0); 
         if (stage >= flower_stage) 
          { 
             if (getCursor() != "harvest") 
              { 
                 sendAction("harvest"); 
                 waitCursor("harvest"); 
              } 
             doClick(id, 1, 0); 
             waitHourglass();
          } 
      } 
  } 
  
 
void plantSeed() {
	def BestItem=null;
	int MaxQ=0;
    for (item in getInventory("Inventory").getItems()) {
        if (isSeed(item)) {
            if (item.getQuality()>MaxQ) {
				MaxQ=item.getQuality();
				BestItem=item;
			}
        }
    }
	if (BestItem==null) return;
	int ix=BestItem.row;
	int iy=BestItem.column;
	BestItem.take();
	waitDrag();
	mapInteractClick(0, 0, 0);
    Thread.sleep 100;
	// If seed planting failed, return it to inventory
	if (hasDragItem())
	 {
		getInventory("Inventory").drop(ix, iy);
	 }
}

static boolean isSeed(item)
{
  if (item.isName("carrot") || 
	  item.isName("peapod") ||
	  item.isName ("seed-grape") || 
	  item.isName ("flaxseed") || 
	  item.isName ("seed-wheat") || 
	  item.isName ("seed-pumpkin") || 
	  item.isName ("seed-carrot") || 
	  item.isName ("seed-pepper") || 
	  item.isName ("seed-hemp") || 
	  item.isName ("seed-hops") || 
	  item.isName ("seed-poppy") || 
	  item.isName ("seed-tea") || 
	  (item.isName ("beetroot") && !item.isName ("leaves") && !item.isName ("Weird"))) 
	return true
   else 
	return false
}


/********************************************************
*****               OTHER                           *****
********************************************************/

void resetCursor() 
 { 
    if (getCursor() != "arw") 
     { 
        mapClick(0,0,3,0); 
        waitCursor("arw"); 
         } 
 } 
 
 void refill_stam()
{

       if( rouse == 1 )
        {
           say(":rouse");
           while(Stamina < 95) Thread.sleep(100);
        }
       if( water == 1 )
        {
           resetCursor();
           mapAbsClick(getMyCoordX(), getMyCoordY(),1, 0);
           drinkWater(true);
           while(Stamina < 90) Thread.sleep(100);
        }
}

void pumpkinate()
{
Thread.sleep(300)
for (item in getInventory("Inventory").getItems())
 {
        if (item.isName("pumpkin"))
        {
         item.act()
         waitContextMenu().select("Slice")
        }
 }
Thread.sleep(300)
for (item in getInventory("Inventory").getItems())
 {
        if (item.isName("flesh")||item.isName("seed")) 
        {
            item.take()
            waitDrag()
            drop()
        }
 }

}