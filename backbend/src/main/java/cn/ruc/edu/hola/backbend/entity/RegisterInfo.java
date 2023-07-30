package cn.ruc.edu.hola.backbend.entity;


public record RegisterInfo(String serviceId, String serviceName, String info, String struct,
                           BackBendConfigInfo backBendInfo) {
}
