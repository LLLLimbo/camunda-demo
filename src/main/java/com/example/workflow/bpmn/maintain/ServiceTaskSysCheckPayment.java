package com.example.workflow.bpmn.maintain;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class ServiceTaskSysCheckPayment implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("用户已确认付款.");
  }
}
