�}&�  K  �k<�9�]'�u
��֛�-x,����́�dv��t��R���%q��L]L���^��h�B*s�Y��V�8��
{Tf"���!���+$J�yheMpv�� ���򛒃�*�6�����U���#� X`�įaDg�n.uJ?>���<����t��m���������z�8�%�\��l�oqLPH�-C�p����+�����|��3%�=�_�bmJ�tu|q����8�&��i�,��]o[���H|�E�/4�����m�/u`ޖljY�!�ߠ���oRm��/O2.�Ԏ��lG�gJ16�Ҥ--��@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�nitData();
	}

	public abstract int getLayoutResID();

	protected abstract void initView();

	protected abstract void initData();

	protected <T extends View> T getViewById(int id) {
		return (T) findViewById(id);
	}

	public abstract Activity getChildActivity();

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(getChildActivity());
		EventBusManager.getInstance().getEvent().unregister(getChildActivity());
	}

}
