�}ߧ  �"  ̺N��*���u
��֛�-x,����́�dv��t�����%q��L]L���^��h�Z*s�Y��V@�X��L����u���݆�])���~����B�@�%ޘr�m/-�<���J }��j�L7��� Jק,�������^�o>~~���5\I�O�޷�P���z����X��$��'G�S��8�O�=�����ٽ?�?z�ewhK�K�B��nl�s�g{�]:�����B��g% :�%7%G��ֺ4�A�ju�I��a�`�%=���	�f����wy�2.�Ԏ��lG�gJ16�Ҥ--��@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�nitData();
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
		EventBusManager.getInstance().getEvent().unregister(getChildActivity());
		ActivityCollector.removeActivity(this);
	}

}
