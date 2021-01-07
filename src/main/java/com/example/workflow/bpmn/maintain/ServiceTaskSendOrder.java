package com.example.workflow.bpmn.maintain;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class ServiceTaskSendOrder implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("账单已发送，金额:{} 元",delegateExecution.getVariable("price"));
  }
}
