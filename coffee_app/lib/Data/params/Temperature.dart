import 'package:coffee_app/Data/BrewParam.dart';

class Temperature extends BrewParam{
  bool isFarenheight = true;
  int temp = 0;


  void setTemp(int temperature) {
    temp = temperature;
  }

  int getTemp() {
    return temp;
  }

  void setIsFarenheight(bool isFarenheight) {
    this.isFarenheight = isFarenheight;
  }

  bool getIsFarenheight() {
    return isFarenheight;
  }


  
 



  

  







}