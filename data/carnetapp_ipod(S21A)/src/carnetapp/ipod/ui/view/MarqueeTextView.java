�}��  (#  �6�;�}��u
��֛W-�,��ݸߡ�`���1�O�*�C�X�R2̣.؜�#o��7��ĭz���*�w��������תȹ�u)k>_���R;.{���µ�Y�hյ>�h�hQv�e�s��M�-ٵ9E|�QP����6d<���B��&ۅV���˸��n/�K�kH}!ǢPM�����Vi(Sz};��q &�8ݧc��&�i�߮e��3�,"�����e����������ٟ�|?����/6���گ@��1����}����o�U'���]����lG�gJ16�Ҥ--��@vC�^���<U)�����0���o=`S|�õT�!��������"z!LA��`��+��?wp�c��RR�}s��褔}��DH��ގ��ƨĮ���s����t̲؍O����]��t7�z2ۈ��[��??�MJ�odS���'�q�	!8�ʋyZ��X�l�3S��YY+��~i䔢.�p���"fh����R�ډ�=�i�P��dɄFrQ��yT�尦-n�T"z�J�b
(�i�~�ǽ6��H�7��%򮾄a8f�W�G^�/JpF륓�N��k!cw�[�M�ڹl�ء�{����F�{̘u�Ke�X��v�C�FP���L���P*�\YvMƐ�������쫄qh\"?�; ��`��shYYj(�Zo��s/������5���uO �����ܐA1���q��1�$��x����L���-S븛�<2~
�z�CU���h�;��|��Ug��b�iB�gZ���kR.��O
Mfڠ���Y{$�^ė��}V��h��0`I�N�f?r߱W9\̣� x�����֏:.!<�d��0����O<�~��#�ǒrL�)�:>��a
�Xd'Y�*@���$��S���v�Ci=��晚�%3l�3��c|�4�z�#��֪ ��,o��9����"@kT#����^���\'IRkҊ��N�ns�4ɶ�6t�hanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	/*
	 * Window与Window间焦点发生改变时的回调
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (hasWindowFocus)
			super.onWindowFocusChanged(hasWindowFocus);
	}
}
