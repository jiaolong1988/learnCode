package base;

/**
 * 
 * @ClassName:  ExceptionTest3   
 * @Description:TODO(测试finally 中return 的返回值)   
 * @author: jiaolong
 * @date:   2022年3月22日 下午2:25:24      
 * @Copyright:
 */
public class ExceptionTest {

	/**
	 * 
	 * @Title:  ExceptionTest3   
	 * @Description: 当程序遇到return或throw语句时，该方法并不会立即结束。当存在finally语句块时则执行finally块代码，没有则立即执行return或throw 语句。
	 * 				 1.finally不管如何都会执行。
	 * 		         2.当finally中存在 return或throw时，结果都已finally中的返回为准，即便try、catch语句块中也存在return或throw语句。
	 * @param:  @param args  
	 * @throws
	 */
	public static void main(String[] args) {
		boolean t11 = tes1();
		System.out.println("result: false test1 = "+t11);
		
		System.out.println("\r\n");
		boolean t12 = tes2();
		System.out.println("result: false test2 = "+t12);
		
		
		System.out.println("\r\n");
		tes3();
	

	}
	

	@SuppressWarnings("finally")
	public static boolean tes1() {	   
	    try {
	    	System.out.println("run try..");
	        return true;   
	        
	    } finally {
	    	System.out.println("run finally..");
	        return false;
	    }
	    
	}

	
	@SuppressWarnings("finally")
	public static boolean tes2() {	   
	    try {
	    	System.out.println("run Exception try..");
	    	throw new RuntimeException();
	    }catch(Exception e) {
	    	System.out.println("run Exception catch..");
	    	//返回不生效
	    	return true;
	    }
	    finally {
	    	System.out.println("run Exception finally..");
	    	//最终返回结果
	        return false;
	    }
	}
	
	@SuppressWarnings("finally")
	public static void tes3() {	   
	    try {
	    	System.out.println("run Exception try..");
	    	//异常不生效
	    	throw new RuntimeException("xxxx");
	    }catch(Exception e) {
	    	System.out.println("run Exception catch..");
	    	//异常不生效
	    	throw new RuntimeException("yyyy");
	    }
	    finally {
	    	System.out.println("run Exception finally..");	
	    	//最终抛出的异常
	    	throw new RuntimeException("zzzz");
	    }
	}
}
