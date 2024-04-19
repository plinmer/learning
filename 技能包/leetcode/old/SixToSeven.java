public class SixToSeven{
	public static void sToSeven(String six){
		int value = 0;
		char temp = ' ';
		int index = 1;
		for(int i = six.length() - 1; i >= 0; i--){
			temp = six.charAt(i);
			if(temp >= '0' && temp <= '9'){
				value = value + index * (temp - '0');
			}
			if(temp >= 'A' && temp <='F'){
				value = value + index * (temp - 'A' + 10);
			}
			index *= 16;
		}
		StringBuilder sb = new StringBuilder("");
		while(value > 0){
			index = value % 7;
			sb.append((char)(index + '0'));
			value /= 7;
		}
		String res = "";
		for(int i = sb.length() - 1;i >= 0;i--){
			res += sb.charAt(i);
		}
		System.out.println(res);
	}
	public static void main(String[] args){
		String six = "A0";
		SixToSeven.sToSeven(six);
	}

}