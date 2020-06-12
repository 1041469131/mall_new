package com.zscat.mallplus.admin.component;

import com.zscat.mallplus.mbg.utils.CommonResult;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author nzy
 */
@Aspect
@Component
public class ControllerAspect {

  private final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

  @Pointcut("execution(* com.zscat.mallplus..*.*Controller.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
  private void pointCutMethod() {
  }


  @Around("pointCutMethod()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    try {
      long startTime = System.currentTimeMillis();
      Object proceed = pjp.proceed();
      long costTime = System.currentTimeMillis() - startTime;
      if(costTime>2000){
        logger.error("signature[{}] cost time:{}", pjp.getSignature(), costTime);
      }else if(costTime>300){
        logger.warn("signature[{}] cost time:{}", pjp.getSignature(), costTime);
      }else if(costTime>100){
        logger.info("signature[{}] cost time:{}", pjp.getSignature(), costTime);
      }

      return proceed;
    }
    catch (UsernameNotFoundException e){
      return dealUsernameNotFoundException(pjp,e);
    }
     catch (Exception e) {
      return dealOtherException(pjp, e);
    }

  }

  private Object dealOtherException(ProceedingJoinPoint pjp, Exception e) {
    logger.error("stackTrace:", e);
    logger.error("error happened while processing method {}, args:{}", pjp.getSignature(), Arrays.toString(pjp.getArgs()));
    return new CommonResult().failed();
  }

  private Object dealUsernameNotFoundException(ProceedingJoinPoint pjp, UsernameNotFoundException e) {
    logger.error("error message {}, args {}", e.getMessage(), Arrays.toString(pjp.getArgs()));
    return new CommonResult().failed(e.getMessage());
  }
}
