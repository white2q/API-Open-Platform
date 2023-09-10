import {ProColumns, ProTable,} from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';
import {Modal} from "antd";

export type Props = {
  columns: ProColumns<API.RuleListItem>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfo) => Promise<void>;
  visible: boolean;
};
const CreateModal: React.FC<Props> = (props) => {
  const {visible, columns, onCancel, onSubmit} = props;

  return (<Modal visible={visible} footer={null} onCancel={() => onCancel?.()}>
    <ProTable
    type= "form"
    columns={columns}
    onSubmit={async (value) => {
      onSubmit?.(value)
    }}
    ></ProTable>
  </Modal>)
}
export default CreateModal;
