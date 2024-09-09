

import 'package:coffee_app/Data/BrewParam.dart';

class BrewRatio extends BrewParam {
double coffee = 0.0;
double water =0.0;

void setCoffee(double amnt) {
  coffee = amnt;
}

void setWater (double amnt) {
  water = amnt;
}

double getWater () {
  return water;
}


double getCoffee () {
  return coffee;
}

double getRatio() {
  return  water == 0 ? 0.0 : coffee/water;
}

void setRatio(double ratio, bool usingWater) {
  if (usingWater) {
    coffee = water/ratio;
  } else {
    water = coffee*ratio;
  }
}



}

