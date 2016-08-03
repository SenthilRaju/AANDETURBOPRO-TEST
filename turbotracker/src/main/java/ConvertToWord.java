import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class ConvertToWord
  extends JRDefaultScriptlet{
//This class file should put in jasperreports path
public static String IntToLetter(int Int) {
    if (Int<27){
      return Character.toString((char)(Int+96)).toUpperCase();
    } else {
      if (Int%26==0) {
        return IntToLetter((Int/26)-1)+IntToLetter(((Int-1)%26+1)).toUpperCase();
      } else {
        return IntToLetter(Int/26)+IntToLetter(Int%26).toUpperCase();
      }
    }
  }
public static String negativevalueinparanthesis(BigDecimal value) {
	DecimalFormat df=new DecimalFormat(" #,##0.00");
   String returnvalue="";
   if(value.compareTo(BigDecimal.ZERO)>0){
	   returnvalue= "$ "+df.format(value.setScale(2,RoundingMode.FLOOR));
	   
   }else if(value.compareTo(BigDecimal.ZERO)<0){
	   returnvalue= "(   $ "+df.format(value.multiply(new BigDecimal(-1)).setScale(2,RoundingMode.FLOOR))+ ")";
   }
	return returnvalue;
  }

}
