//
//  OpenForecast - open source, general-purpose forecasting package.
//  Copyright (C) 2002-2003  Steven R. Gould
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

package net.sourceforge.openforecast.models;


import java.util.Iterator;

import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;


/**
 * Implements a single variable linear regression model using the variable
 * named in the constructor as the independent variable. The cofficients of
 * the regression - the intercept and the slope - as well as the accuracy
 * indicators are determined from the data set passed to init.
 *
 * <p>Once initialized, this model can be applied to another data set using
 * the forecast method to forecast values of the dependent variable based on
 * values of the dependent variable (the one named in the constructor).
 *
 * <p>A single variable linear regression model essentially attempts to put a
 * straight line through the data points. For the more mathematically inclined,
 * this line is defined by its gradient or slope, and the point at which it
 * intercepts the x-axis (i.e. where the independent variable has, perhaps only
 * theoretically, a value of zero). Mathematically, assuming the independent
 * variable is x and the dependent variable is y, then this line can be
 * represented as: <pre>y = intercept + slope * x</pre>
 * @author Steven R. Gould
 */
public class RegressionModel extends AbstractForecastingModel
{
	 /**
	  * The name of the independent variable used in this regression model.
	  */
	 private String independentVariable;

	 /**
	  * The intercept for the linear regression model. Initialized following a
	  * call to init.
	  */
	 private double intercept = 0.0;

	 /**
	  * The slope for the linear regression model. Initialized following a call
	  * to init.
	  */
	 private double slope = 0.0;

	 /**
	  * Constructs a new linear regression model, using the given name as the
	  * independent variable. For a valid model to be constructed, you should
	  * call init and pass in a data set containing a series of data points
	  * involving the given independent variable.
	  * @param independentVariable the name of the independent variable to use
	  * in this model.
	  */
	 public RegressionModel( String independentVariable )
	 {
		  this.independentVariable = independentVariable;
	 }

	 /**
	  * Initializes the coefficients to use for this regression model. The
	  * intercept and slope are derived so as to give the best fit line for the
	  * given data set.
	  *
	  * <p>Additionally, the accuracy indicators are calculated based on this
	  * data set.
	  * @param dataSet the set of observations to use to derive the regression
	  * coefficients for this model.
	  */
	 public void init( DataSet dataSet )
	 {
		  int n = dataSet.size();
		  double sumX = 0.0;
		  double sumY = 0.0;
		  double sumXX = 0.0;
		  double sumXY = 0.0;
		  
		  Iterator it = dataSet.iterator();
		  while ( it.hasNext() )
				{
					 DataPoint dp = (DataPoint)it.next();
					 
					 double x = dp.getIndependentValue( independentVariable );
					 double y = dp.getDependentValue();
					 
					 sumX += x;
					 sumY += y;
					 sumXX += x*x;
					 sumXY += x*y;
				}
		  
		  double xMean = sumX / n;
		  double yMean = sumY / n;
		  
		  slope = (n*sumXY - sumX*sumY) / (n*sumXX - sumX*sumX);
		  intercept = yMean - slope*xMean;
		  
		  // Calculate the accuracy of this model
		  calculateAccuracyIndicators( dataSet );
	 }

	 /**
	  * Using the current model parameters (initialized in init), apply the
	  * forecast model to the given data point. The data point must have valid
	  * values for the independent variables. Upon return, the value of the
	  * dependent variable will be updated with the forecast value computed for
	  * that data point.
	  * @param dataPoint the data point for which a forecast value (for the
	  *        dependent variable) is required.
	  * @return the same data point passed in but with the dependent value
	  *         updated to contain the new forecast value.
	  * @throws ModelNotInitializedException if forecast is called before the
	  *         model has been initialized with a call to init.
	  */
	 public double forecast( DataPoint dataPoint )
		  throws ModelNotInitializedException
	 {
		  if ( !initialized )
				throw new ModelNotInitializedException();
		  
		  double x = dataPoint.getIndependentValue( independentVariable );
		  double forecastValue = intercept + slope*x;
		  
		  dataPoint.setDependentValue( forecastValue );
		  
		  return forecastValue;
	 }
	 
	 /**
	  * Returns a short name for this type of forecasting model. A more detailed
	  * explanation is provided by the toString method.
	  * @return a short string describing this type of forecasting model.
	  */
	 public String getForecastType()
	 {
		  return "Single variable regression";
	 }
	 
	 /**
	  * Returns a detailed description of this forcasting model, including the
	  * intercept and slope. A shortened version of this is provided by the
	  * getForecastType method.
	  * @return a description of this forecasting model.
	  */
	 public String toString()
	 {
		  return "Single variable regression model with a slope of " + slope
				+ " and an intercept of " + intercept
				+ ". That is, y=" + intercept + (slope>0.0 ? "+" : "") + slope
				+ "*" + independentVariable + ".";
	 }
}
