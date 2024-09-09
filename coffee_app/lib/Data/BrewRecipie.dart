import 'package:coffee_app/Data/BrewParam.dart';
import 'package:coffee_app/Data/Graph.dart';
import 'package:coffee_app/Data/params/BrewRatio.dart';
import 'package:coffee_app/Data/params/Temperature.dart';

class Brewrecipie {
  BrewParam ratio = BrewRatio();
  BrewParam temperature = Temperature();
  Graph flowRate = Graph();


}